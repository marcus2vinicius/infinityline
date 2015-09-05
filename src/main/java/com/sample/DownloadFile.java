package com.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile {

	String status = null;

	public void download() {

		try {
			File f = new File("C:\\file.mp3");

			if (f.exists()) {
				f.delete();
				System.out.println("Old file deleted!");
			}

			URLConnection conn = new URL(
					"http://www.tonycuffe.com/mp3/tail%20toddle.mp3")
					.openConnection();
			InputStream is = conn.getInputStream();

			OutputStream outstream = new FileOutputStream(f);
			byte[] buffer = new byte[4096];
			int len;
			while ((len = is.read(buffer)) > 0) {
				outstream.write(buffer, 0, len);
			}
			outstream.close();
			System.out.println("Download complete");

			if (f.exists()) {
				status = "Download successful.";
			} else {
				status = "Download failed.";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getStatus() {
		return status;
	}
}
