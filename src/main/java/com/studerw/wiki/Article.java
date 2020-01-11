package com.studerw.wiki;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Article based on wikimedia dump in format of multiple JSON blocks of the form:
 * <pre>
 *  {@code "id": "12", "url": "https://en.wikipedia.org/wiki?curid=12", "title": "Anarchism", text="Anarchism\n\n...\n\n\n"}
 * </pre>
 *
 */
public class Article {
	private static final Logger LOGGER = LoggerFactory.getLogger(Article.class);

	Long id;

	String url;

	String title;

	String text;

	public Article() {
	}

	public Article(Long id, String url, String title, String text) {
		this.id = id;
		this.url = url;
		this.title = title;
		this.text = text;
	}

	/**
	 *
	 * @return article id
	 */
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 *
	 * @return article url
	 */
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 *
	 * @return article title
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return parsed text, though it contains numerous extra newlines and the title it prepended.
	 */
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	/**
	 *
	 * @return text without the extra newlines and prepended title
	 */
	public String getTrimmedText() {
		List<String> lines = new ArrayList<>();
		try (final InputStream inputStream = IOUtils.toInputStream(this.getText(), StandardCharsets.UTF_8)) {
			final LineIterator lineIterator = IOUtils.lineIterator(inputStream, StandardCharsets.UTF_8);
			//remove the title first line
			lineIterator.next();
			while (lineIterator.hasNext()) {
				lines.add(lineIterator.next());
			}
			lineIterator.close();
			String trimmed = lines.stream().collect(Collectors.joining()).trim();
			return trimmed;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
				.append("id", id)
				.append("url", url)
				.append("title", title)
				.append("text bytes", text.length())
				.toString();
	}
}
