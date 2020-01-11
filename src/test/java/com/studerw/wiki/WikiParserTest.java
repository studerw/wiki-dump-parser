package com.studerw.wiki;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WikiParserTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(WikiParserTest.class);
	private static final WikiParser wikiParser = new WikiParser();
	private int articleCnt = 36;
	private int totalArticleCtn = 116;

	@Test
	public void parseByFile() {
		final URL resource = getClass().getClassLoader().getResource("json/wiki_00");
		String path = resource.getFile();
		LOGGER.debug(resource.getFile());
		final List<Article> articles = wikiParser.parseByFile(path);
		articles.stream().forEach(article -> LOGGER.debug("{}", articles));
		LOGGER.debug("Size of articles: {}", articles.size());
		assertThat(articles).size().isEqualByComparingTo(articleCnt);
	}

	@Test
	public void parseByJavaFile() {
		final URL resource = getClass().getClassLoader().getResource("json/wiki_00");
		LOGGER.debug(resource.getFile());
		String path = resource.getFile();
		final List<Article> articles = wikiParser.parseByFile(path);
		articles.stream().forEach(article -> LOGGER.debug("{}", articles));
		LOGGER.debug("Size of articles: {}", articles.size());
		assertThat(articles).size().isEqualByComparingTo(articleCnt);
	}

	@Test
	public void parseByDirectory() {
		final URL resource = getClass().getClassLoader().getResource("json");
		LOGGER.debug(resource.getFile());
		String path = resource.getFile();
		final List<Article> articles = wikiParser.parseByDirectory(path);
		LOGGER.debug("Size of articles: {}", articles.size());
		assertThat(articles).size().isEqualByComparingTo(totalArticleCtn);
	}
}