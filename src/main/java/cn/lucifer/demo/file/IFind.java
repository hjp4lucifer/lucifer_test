package cn.lucifer.demo.file;

import java.io.File;
import java.io.IOException;
import java.util.List;

interface IFind {

	void find(File file, List<String> lines) throws IOException;
}
