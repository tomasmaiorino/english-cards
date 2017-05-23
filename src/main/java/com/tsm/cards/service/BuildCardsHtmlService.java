package com.tsm.cards.service;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.tsm.cards.model.Cards;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BuildCardsHtmlService {

    @Getter
    @Setter
    public String cardTemplatePath;

    public void createCardHtml(final Cards ards) {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        try {
            PebbleTemplate compiledTemplate = engine.getTemplate("templates/home.html");

            Writer writer = new StringWriter();

            Map<String, Object> context = new HashMap<>();
            configureTemplateParams(context);

            compiledTemplate.evaluate(writer, context);

            String output = writer.toString();

        } catch (PebbleException | IOException e) {
            e.printStackTrace();
        }
    }

    private void configureTemplateParams(Map<String, Object> context) {
        context.put("websiteTitle", "My First Website");
        context.put("content", "My Interesting Content");
    }
}
