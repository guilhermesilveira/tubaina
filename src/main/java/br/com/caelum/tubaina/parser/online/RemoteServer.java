package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.thoughtworks.xstream.XStream;

public class RemoteServer {

	private static final String REMOTE_PATH = "/admin/8921d89ujdwh897u234hu/tubaina";
	private final String server;
	private final String extraParameter;

	public RemoteServer(String server, String extraParameter) {
		this.server = server;
		this.extraParameter = extraParameter;
	}

	public void sync(GnarusCourse course) {
		try {
			String xml = toXml(course);

			PrintStream backup = new PrintStream(new File(
					"_sending_to_online.xml"), AfcSplitFiles.FILE_ENCODING);
			backup.println(xml);
			backup.close();

			DefaultHttpClient client = new DefaultHttpClient();
			List<NameValuePair> params = new ArrayList<>();
			params.add(new BasicNameValuePair("xml", xml));
			params.add(new BasicNameValuePair("extra", extraParameter));

			HttpPost post = new HttpPost("http://" + server + REMOTE_PATH);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
					"UTF-8");
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code != 200) {
				String body = EntityUtils.toString(response.getEntity());
				throw new RuntimeException("Error code " + code + "\n\n\n"
						+ body);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String toXml(GnarusCourse course) {
		XStream xs = new XStream();
		xs.alias("tubainaCourse", GnarusCourse.class);
		xs.alias("tubainaSection", GnarusSection.class);
		xs.alias("tubainaExercise", GnarusExercise.class);
		return xs.toXML(course);
	}
}
