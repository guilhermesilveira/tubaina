package br.com.caelum.tubaina.parser.online;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

class Parameters {
	private final Map<String, String> params = new HashMap<>();
	{
		params.put("server", "localhost:8080/gnarus");
		params.put("extraParameter", "");
		params.put("path", ".");
		params.put("code", "");
		params.put("ignore", "00");
		params.put("splitPerSection", "false");
	}

	Parameters(String[] args) {
		for (String arg : args)
			parse(arg);
	}

	private void parse(String arg) {
		int equalsPosition = arg.indexOf("=");
		if (equalsPosition == -1) {
			System.err.println("Unknown arg: " + arg);
			System.exit(1);
		}

		String argName = arg.substring(1, equalsPosition);
		String value = arg.substring(equalsPosition + 1);
		if (params.containsKey(argName)) {
			params.put(argName, value);
		} else {
			System.err.println("Unknown arg: " + argName);
			System.exit(1);
		}
	}

	public String get(String key) {
		return params.get(key);
	}

	public boolean getBoolean(String key) {
		return Boolean.valueOf(get(key));
	}

	public File getFile(String key) {
		return new File(get(key));
	}
}