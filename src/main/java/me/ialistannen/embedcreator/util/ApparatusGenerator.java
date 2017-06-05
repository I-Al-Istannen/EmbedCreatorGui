package me.ialistannen.embedcreator.util;

import me.ialistannen.embedcreator.extraction.Generator;
import me.ialistannen.embedcreator.extraction.ProviderCollection;
import me.ialistannen.embedcreator.extraction.TextWithUrl;
import me.ialistannen.embedcreator.view.EmbedField;

import java.util.List;

import static me.ialistannen.embedcreator.extraction.ProviderCollection.ProviderType;

public class ApparatusGenerator implements Generator {

    @Override
    public String generate(ProviderCollection data) {
        String res = "";
        for (ProviderType providerType : ProviderType.values()) {
            switch (providerType) {
                case FIELDS: {
                    res += parseEmbed(data.get(providerType)) + " ";
                    break;
                }
                case COLORED_BAR: {
                    if (!data.get(providerType).isEmpty()) {
                        String hex = Integer.toHexString(Integer.parseInt(data.get(providerType).get(0).toString().replace("0x", ""), 16));
                        res += "{Color:#" + hex.substring(0, hex.length() - 2) + "} ";
                    }
                    break;
                }
                case AUTHOR_TEXT: {
                    if (!data.get(providerType).isEmpty()) {
                        List<TextWithUrl> list = data.get(providerType);
                        TextWithUrl author = list.get(0);
                        res += "{AuthorName:" + author.getText() + "} ";
                        if (author.getUrl() != null) {
                            res += "{AuthorURL:" + author.getUrl() + "} ";
                        }
                    }
                    break;
                }

                case AUTHOR_IMAGE: {
                    if (!data.get(providerType).isEmpty()) {
                        if (!data.get(providerType).get(0).toString().isEmpty()) {
                            res += "{AuthorIcon:" + data.get(providerType).get(0).toString() + "} ";
                        }
                    }
                    break;
                }

                case THUMBNAIL_IMAGE: {
                    if (!data.get(providerType).isEmpty()) {
                        if (!data.get(providerType).get(0).toString().isEmpty()) {
                            res += "{Thumbnail:" + data.get(providerType).get(0).toString() + "} ";
                        }
                    }
                    break;
                }

                case TITLE: {
                    if (!data.get(providerType).isEmpty()) {
                        List<TextWithUrl> list = data.get(providerType);
                        TextWithUrl title = list.get(0);
                        res += "{Title:" + title.getText() + "} ";
                        if (title.getUrl() != null)
                            res += "{TitleURL:" + title.getUrl() + "} ";
                    }
                    break;
                }

                case DESCRIPTION: {
                    if (!data.get(providerType).isEmpty()) {
                        res += "{Description:" + data.get(providerType).get(0).toString() + "} ";
                    }
                    break;
                }

                case IMAGE: {
                    if (!data.get(providerType).isEmpty()) {
                        if (!data.get(providerType).get(0).toString().isEmpty()) {
                            res += "{Image:" + data.get(providerType).get(0).toString() + "} ";
                        }
                    }
                    break;
                }
                case FOOTER_IMAGE: {
                    if (!data.get(providerType).isEmpty()) {
                        if (!data.get(providerType).get(0).toString().isEmpty()) {
                            res += "{FooterIcon:" + data.get(providerType).get(0).toString() + "} ";
                        }
                    }
                    break;
                }
                case FOOTER_TEXT: {
                    if (!data.get(providerType).isEmpty()) {
                        res += "{FooterText:" + data.get(providerType).get(0).toString() + "} ";
                    }
                    break;
                }
            }
        }
        return res.trim();
    }

    private String parseEmbed(List<EmbedField> embeds) {
        String res = "";
        for (EmbedField em : embeds) {
            res += "{FieldTitle:" + em.getName() + " FieldText:" + em.getValue() + " Inline:" + em.isInline() + "} ";
        }
        return res.replaceAll("", "").trim();
    }


}