package org.spletneseje.parser.datastruct;

import org.junit.Test;
import org.spletneseje.parser.AbsParser;
import org.spletneseje.parser.LogFormats;
import org.spletneseje.parser.NCSAParser;

import java.io.FileNotFoundException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class WebPageTest {

    private WebPage page;

    @Test
    public void testOne() throws FileNotFoundException {
        String testNiz = "46.122.131.143 - - [26/Jun/2014:04:46:13 +0200] \"GET /hlace?limit=30 HTTP/1.1\" 200 8452\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /catalog/view/theme/sfashion/stylesheet/stylesheet.css HTTP/1.1\" 200 6486\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /catalog/view/theme/sfashion/stylesheet/carousel.css HTTP/1.1\" 200 778\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /catalog/view/javascript/jquery/supermenu/templates/gray/supermenu.css HTTP/1.1\" 200 2139\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /catalog/view/javascript/jquery/supermenu/supermenu_base.js HTTP/1.1\" 200 10179\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:15 +0200] \"GET /catalog/view/javascript/common.js HTTP/1.1\" 200 1769\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:15 +0200] \"GET /catalog/view/javascript/jquery/jquery.total-storage.min.js HTTP/1.1\" 200 1098\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /image/cache/data/linographYE-cr-214x293.png HTTP/1.1\" 200 87040\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:14 +0200] \"GET /image/cache/data/wilmslowXBLU3-cr-214x293.png HTTP/1.1\" 200 82960\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:15 +0200] \"GET /catalog/view/javascript/jquery/ui/themes/ui-lightness/jquery-ui-1.8.16.custom.css HTTP/1.1\" 200 4808\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:15 +0200] \"GET /catalog/view/javascript/jquery/jquery.jcarousel.min.js HTTP/1.1\" 200 4576\n" +
                "46.122.131.143 - - [26/Jun/2014:04:46:15 +0200] \"GET /image/cache/data/rockfordNV-cr-214x293.png HTTP/1.1\" 200 43520";
        AbsParser parser = new NCSAParser();
        parser.setFieldType(LogFormats.CommonLogFormat.create(null));
        parser.openFile(new StringReader(testNiz));
        for (ParsedLine line : parser) {
            if (page == null) {
                page = new WebPage(line);
            } else {
                assertTrue(page.add(line));
            }
        }
        System.out.println(page.toString());
    }

}