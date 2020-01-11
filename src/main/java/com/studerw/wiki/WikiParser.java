package com.studerw.wiki;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiParser {
	private static final Logger LOGGER = LoggerFactory.getLogger(WikiParser.class);

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 *
	 * @param filePath explicit path of a single json file of WikiExtractor json dumped text
	 * @return List of articles with id, text, title, and url
	 */
	List<Article> parseByFile(String filePath) {
		LOGGER.debug("Getting articles from file with path: {}", filePath);
		File file = new File(filePath);
		return parseByJavaFile(file);
	}

	/**
	 *
	 * @param file single json file of WikiExtractor json dumped text
	 * @return List of articles with id, text, title, and url
	 */
	List<Article> parseByJavaFile(File file) {
		LOGGER.debug("Getting articles from java file with path: {}", file);
		List<Article> articles = new ArrayList<>();

		try (final FileInputStream fileInputStream = FileUtils.openInputStream(file)) {
			final LineIterator iter = IOUtils.lineIterator(fileInputStream, Charset.forName("UTF-8"));
			while (iter.hasNext()) {
				final String line = iter.nextLine();
				final Article article = mapper.readValue(line, Article.class);
				LOGGER.debug("{}", article);
				articles.add(article);
			}
			LOGGER.debug("Processed {} articles,", articles.size());
			return articles;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 *
	 * @param dirPath directory path of directory containing json files of WikiExtractor json dumped text
	 * @return List of articles with id, text, title, and url
	 */
	List<Article> parseByDirectory(String dirPath) {
		LOGGER.debug("Getting articles from directory : {}", dirPath);
		try {
			final List<Article> articles = Files.walk(Paths.get(dirPath))
					.filter(Files::isRegularFile)
					.map(path -> path.toFile())
					.map(this::parseByJavaFile)
					.flatMap(List::stream)
					.collect(Collectors.toList());
			LOGGER.debug("Total articles: {}", articles.size());
			return articles;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
