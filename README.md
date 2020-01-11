# Wiki Dump Parser
![WIKIPEDIA_LOGO](https://github.com/studerw/wiki-dump-parser/blob/master/wikipedia_logo.png)

![travisci-passing](https://api.travis-ci.org/studerw/wiki-dump-parser.svg?branch=master)
[![APL v2](https://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)


----
This is a small utility to create random text files to use for loading test data into Elastic Search or a CMS system.
It uses Wikipedia to get a massive amount of documents from the dumps you can download and extract, parses them,
and returns them to use as Java pojoso of type _Article_ which contains the article's text, id, url, trimmed text,
and title. 

To get one or more dump files, I have successfully used this mirror: [Latest English Wiki Dump](https://dumps.wikimedia.org/enwiki/latest/).

The dump is divided up into dozens of bzipped archives. This tool expects that you've downloaded the current archive files (i.e.)
without all the revisions and metadata. 

For example, to download a single archive of a few hundred MB, eventually parsing into several hundred Wikipedia articles,
do the following:

* Go to [Latest English Wiki Dump](https://dumps.wikimedia.org/enwiki/latest/).
* Download files in the format of `enwiki-latest-pages-articles{n}.xml-{random_hash}.bz2`, where `n` while be some index from 1 to about 30 or so. 
  - Note that you don't need the files in the format `enwiki-latest-pages-articles-multistream{n}.xml-{random_hash}.bz2`.
* Download one of these files, for example `enwiki-latest-pages-articles1.xml-{random_hash}.bz2`.
* [Bunzip2](https://linux.die.net/man/1/bunzip2) the downloaded file: `bunzip2 <file>`. The `bz2` extension will be removed, and the resulting file will now
be much larger. 
* Checkout the [WikiExtractor](https://github.com/attardi/wikiextractor) Python tool, e.g. `git clone https://github.com/attardi/wikiextractor.git`.
* I used Python 3.7, but supposed 2.x works too. From the tool's directory, run `python3 WikiExtractor.py  --json  -o output enwiki-latest-pages-articles1.xml-{random_hash}` 
* The `output` folder will contain several directories of the articles first letter, e.g. _AA_ or _AB_ and inside each of these folders
will be dozens more files named `wiki_00`, `wiki_01`, etc.
* Inside each one of these files is dozens are JSON objects describing an article. This tool takes a directory, finds all the files, and returns
you a list of _Articles_ which you could then push to ElasticSearch or anywhere else.
    
## Quick WikiExtractorfor Dump File
This was more fully described above, but for quick reference:
```
bunzip2 enwiki-latest-pages-articles{n}.xml-{random_hash}.bz2
python3 WikiExtractor.py  --json  -o output_json_1 enwiki-latest-pages-articles1.xml-p10p30302
```

## Build

To build the jar, checkout the source and run:

```bash
mvn clean install
```

## Usage

Once, you've built it and installed or deployed it to your own repository, 
add the following to your Maven build file:

```
  <dependency>
    <groupId>com.studerw.wiki</groupId>
    <artifactId>wiki-dump-parser</artifactId>
    <version>1.0.0-SNAPSHOT</version>
  </dependency>
```

Use the _WikiParser_ class as follows:

```java
    Stream<Article> articles = wikiParser.parseByDirectory("./output");
    articles.forEach(article -> System.out.println("Title: "+ article.getTitle());
```

## Examples
There are examples of json files in `src/test/resources/json` and usage of the tool in `src/test/com/studerw/wiki/WikiParserTest`.

## Logging
The API uses [SLF4J](http://www.slf4j.org/).
You can use any implementation like Logback or Log4j.

Specific Loggers that you can tune:

* `com.studerw.wiki` - set to debug for parsing output.
