package parser;

import model.Gem;

import java.util.List;

public interface GemParser {
    List<Gem> parse(String xmlFilePath) throws Exception;
}

