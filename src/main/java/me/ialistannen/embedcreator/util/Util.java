package me.ialistannen.embedcreator.util;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import me.ialistannen.embedcreator.model.Variable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Some misc utility methods.
 */
public class Util {

    /**
     * Adds a {@link ContextMenu} to a {@link Node}.
     *
     * @param node                The {@link Node} to attach it to
     * @param contextMenuSupplier The {@link Supplier} to generate a {@link ContextMenu} to attach
     */
    public static void setContextMenu(Node node, Supplier<ContextMenu> contextMenuSupplier) {
        node.setOnContextMenuRequested(event -> {
            if (node.getUserData() != null) {
                return;
            }
            node.setUserData("opened");

            ContextMenu contextMenu = contextMenuSupplier.get();
            contextMenu.setAutoHide(true);
            contextMenu.setOnHidden(hidden -> node.setUserData(null));

            contextMenu.show(node, event.getScreenX(), event.getScreenY());
        });
    }

    public static List<Variable> getVariables() throws IOException {
        List<Variable> res = new ArrayList<>();
        String[] regex = new String[]{"<(\\/)?pre>", "<(\\/)?html>", "<(\\/)?head>", "<(\\/)?body>",
                "(<pre .+>)\\["};
        URL url = new URL("https://raw.githubusercontent.com/NegotiumBots/ApparatusWiki/master/variables.md");
        URLConnection con = url.openConnection(); // (HttpURLConnection)
        con.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0");
        InputStream is = con.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        String fin = "";
        while ((line = br.readLine()) != null) {
            fin += line;
        }
        for (String str : regex) {
            if (str.startsWith("(pre")) {
                fin.replaceAll(str, "[");
            } else {
                fin.replaceAll(str, "");
            }
        }

        String[] split = fin.split(":");
        for (String str : split) {
            String[] args = str.replaceAll("[{}]", "").split(";");
            res.add(new Variable(args[0], Integer.parseInt(args[1])));
        }
        return res;
    }

}
