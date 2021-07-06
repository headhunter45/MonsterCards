package com.majinnaibu.monstercards.helpers;

import org.commonmark.node.Document;
import org.commonmark.node.Node;
import org.commonmark.node.Paragraph;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public final class CommonMarkHelper {
    public static String toHtml(String rawCommonMark) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(rawCommonMark);
        Node parent1 = document.getFirstChild();
        Node parent2 = document.getLastChild();
        if (parent1 == parent2 && parent1 instanceof Paragraph) {
            document = new Document();
            Node child = parent1.getFirstChild();
            while (child != null) {
                Node nextChild = child.getNext();
                document.appendChild(child);
                child = nextChild;//child.getNext();
            }
        }
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
