package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.thoughtworks.xstream.XStream;

public class Gnarus {

//	private static final String GNARUS_URL = "http://online.caelum.com.br/kjfgfjhxgdxbghbdxhvbcfhuxvfuvydfuxybgfhkszd/tubaina";
	private static final String GNARUS_URL = "http://localhost:8080/gnarus/kjfgfjhxgdxbghbdxhvbcfhuxvfuvydfuxybgfhkszd/tubaina";

	public void sync(GnarusCourse course) {
		try {
			String xml = toXml(course);
			
			PrintStream backup = new PrintStream(new File("./gnarus.xml"));
			backup.println(xml);
			backup.close();

			DefaultHttpClient client = new DefaultHttpClient();
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("xml",xml));
			
			HttpPost post = new HttpPost(GNARUS_URL);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,"UTF-8");
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode()!=200){
				throw new RuntimeException(EntityUtils.toString(response.getEntity()));
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
		String xml = xs.toXML(course);
		return xml;
	}
}
