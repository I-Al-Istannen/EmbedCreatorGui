package me.ialistannen.embedcreator.util;

import static me.ialistannen.embedcreator.extraction.ProviderCollection.ProviderType;

import java.util.List;
import javafx.scene.paint.Color;
import me.ialistannen.embedcreator.extraction.Generator;
import me.ialistannen.embedcreator.extraction.ProviderCollection;
import me.ialistannen.embedcreator.extraction.TextWithUrl;
import me.ialistannen.embedcreator.view.EmbedField;

public class ApparatusGenerator implements Generator {

  // Performance is likely not worth obfuscating the layout
  @SuppressWarnings("StringConcatenationInLoop")
  @Override
  public String generate(ProviderCollection data) {
    String res = "";
    for (ProviderType providerType : ProviderType.values()) {

      // skip empty ones, they just disturb stuff
      if (data.get(providerType).isEmpty()) {
        continue;
      }

      switch (providerType) {
        case FIELDS: {
          res += parseEmbed(data.get(providerType)) + " ";
          break;
        }
        case COLORED_BAR: {
          String hex = colorToHexString(data.getSingle(providerType));
          res += "{Color:#" + hex + "} ";
          break;
        }
        case AUTHOR_TEXT: {
          TextWithUrl author = data.getSingle(providerType);

          res += "{AuthorName:" + author.getText() + "} ";
          if (author.getUrl() != null) {
            res += "{AuthorURL:" + author.getUrl() + "} ";
          }
          break;
        }
        case AUTHOR_IMAGE: {
          String url = data.getSingle(providerType);
          if (!url.isEmpty()) {
            res += "{AuthorIcon:" + url + "} ";
          }
          break;
        }
        case THUMBNAIL_IMAGE: {
          String url = data.getSingle(providerType);
          if (!url.isEmpty()) {
            res += "{Thumbnail:" + url + "} ";
          }
          break;
        }
        case TITLE: {
          TextWithUrl title = data.getSingle(providerType);
          res += "{Title:" + title.getText() + "} ";

          if (title.getUrl() != null) {
            res += "{TitleURL:" + title.getUrl() + "} ";
          }
          break;
        }
        case DESCRIPTION: {
          res += "{Description:" + data.getSingle(providerType) + "} ";
          break;
        }
        case IMAGE: {
          String url = data.getSingle(providerType);
          if (!url.isEmpty()) {
            res += "{Image:" + url + "} ";
          }
          break;
        }
        case FOOTER_IMAGE: {
          String url = data.getSingle(providerType);
          if (!url.isEmpty()) {
            res += "{FooterIcon:" + url + "} ";
          }
          break;
        }
        case FOOTER_TEXT: {
          res += "{FooterText:" + data.getSingle(providerType) + "} ";
          break;
        }
      }
    }
    return res.trim();
  }

  private String parseEmbed(List<EmbedField> embeds) {
    StringBuilder res = new StringBuilder();
    for (EmbedField em : embeds) {
      res.append("{FieldTitle:")
          .append(em.getName())
          .append(" FieldText:")
          .append(em.getValue())
          .append(" Inline:")
          .append(em.isInline())
          .append("} ");
    }
    return res.toString().trim();
  }

  /**
   * @param color The input {@link Color}
   * @return The hex string for the color
   */
  private String colorToHexString(Color color) {
    return toHexString((int) (color.getRed() * 255))
        + toHexString((int) (color.getGreen() * 255))
        + toHexString((int) (color.getBlue() * 255));
  }

  /**
   * @param number The number to convert to to a hex string
   * @return The hex string. Minimum length is 2, zero padded
   */
  private String toHexString(int number) {
    String hexString = Integer.toHexString(number);
    if (hexString.length() < 2) {
      return "0" + hexString;
    }
    return hexString;
  }
}