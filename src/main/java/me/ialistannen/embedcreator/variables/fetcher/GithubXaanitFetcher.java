package me.ialistannen.embedcreator.variables.fetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.ialistannen.embedcreator.variables.Variable;
import me.ialistannen.embedcreator.variables.VariableFetcher;

/**
 * Fetches {@link Variable}s from Xaanit's github
 */
public class GithubXaanitFetcher implements VariableFetcher {

  private static final Logger LOGGER = Logger.getLogger("GithubXaanitFetcher");
  private static final String URL_STRING = "https://raw.githubusercontent.com/NegotiumBots/"
      + "ApparatusWiki/master/variables.md";

  @Override
  public Collection<Variable> fetch() {
    try {
      return fetchVariablesImpl();
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error fetching Variables", e);
    }
    return Collections.emptyList();
  }

  private Collection<Variable> fetchVariablesImpl() throws IOException {
    List<Variable> variableList = new ArrayList<>();

    URL url = new URL(URL_STRING);
    URLConnection urlConnection = url.openConnection();
    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
    try (InputStream inputStream = urlConnection.getInputStream();
        Scanner scanner = new Scanner(inputStream)) {

      scanner.useDelimiter(":");
      while (scanner.hasNext()) {
        String next = scanner.next().trim().replace("\n", "");
        Optional<Variable> variable = parseVariable(next);
        if (variable.isPresent()) {
          variableList.add(variable.get());
        } else {
          LOGGER.warning("Unable to parse a variable. Value: '" + next + "'");
        }
      }
    }

    return variableList;
  }

  /**
   * Tries to parse a {@link Variable} from the input string.
   *
   * @param input The input string. Format is {@code {name;length}}
   * @return The parsed {@link Variable}, if possible
   */
  private Optional<Variable> parseVariable(String input) {
    // format is {name;length}

    if (input.length() <= 3) {
      // brackets and semicolon
      return Optional.empty();
    }

    String[] parts = input
        .substring(1, input.length() - 1)
        .split(";");

    if (parts.length != 4) {
      return Optional.empty();
    }

    OptionalInt length = parseInt(parts[1]);
    if (!length.isPresent()) {
      return Optional.empty();
    }
    String description = parts[2];
    boolean picture = Boolean.parseBoolean(parts[3]);

    return Optional.of(new Variable(parts[0], length.getAsInt(), description, picture));
  }

  private OptionalInt parseInt(String input) {
    try {
      return OptionalInt.of(Integer.parseInt(input));
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    }
  }
}
