package me.ialistannen.embedcreator.view;

import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import me.ialistannen.embedcreator.controller.MainScreenController;
import me.ialistannen.embedcreator.extraction.Generator;
import me.ialistannen.embedcreator.extraction.ProviderCollection;
import me.ialistannen.embedcreator.extraction.ProviderCollection.ProviderType;
import me.ialistannen.embedcreator.extraction.TextWithUrl;
import me.ialistannen.embedcreator.validation.CharacterLimit;
import me.ialistannen.embedcreator.validation.ValidationEvent;
import me.ialistannen.embedcreator.validation.ValidationEvent.ValidationResult;
import me.ialistannen.embedcreator.variables.VariableRegistry;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The control panel.
 */
public class ControlPanel extends BorderPane implements Consumer<ValidationEvent> {

    @FXML
    private Label statusValue;

    @FXML
    private Button addField;

    @FXML
    private Button generateButton;

    private MainScreenController mainScreenController;
    private Generator generator = new DebugPrintingGenerator();
    private ProviderCollection providerCollection;

    /**
     * Creates a new ControlPanel.
     */
    public ControlPanel() {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/fxml/controls/ControlPanel.fxml")
        );
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException("Error loading Control panel", e);
        }
    }

    @FXML
    void onAddField(ActionEvent event) {
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(
                "Inline", "Inline", "Block"
        );
        choiceDialog.setTitle("What do you want?");
        choiceDialog.setHeaderText("What type of field do you want?\n"
                + "You can change it via right click later on.");
        Optional<String> fieldType = choiceDialog.showAndWait();
        fieldType.ifPresent(string -> {
            boolean inline = string.equalsIgnoreCase("Inline");
            mainScreenController.addDummyField(inline);
        });
    }


    @FXML
    void onViewAllVariables(ActionEvent event) {
        VariableList variableList = new VariableList();
        variableList.getItems().addAll(VariableRegistry.getAllVariables());

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Variable list");
        alert.setHeaderText("Here is a list with all Variables");
        variableList.setPadding(new Insets(20));
        alert.getDialogPane().setContent(variableList);

        alert.setResizable(false);

        alert.show();
    }

    @FXML
    void onGenerate(ActionEvent event) {
        String generatedCommand = generator.generate(providerCollection);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Generation finished");
        alert.setHeaderText("Here is your generated command.\nCopy-paste it into discord.");

        TextArea textArea = new TextArea(generatedCommand);
        textArea.setFont(Font.font("Monospaced"));
        textArea.setEditable(false);
        textArea.selectAll();

        alert.getDialogPane().setContent(textArea);

        alert.show();

        textArea.requestFocus();
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Sets the {@link MainScreenController} this {@link ControlPanel} should use.
     *
     * @param mainScreenController The {@link MainScreenController}
     */
    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    /**
     * @param providerCollection The {@link ProviderCollection} to use.
     */
    public void setProviderCollection(ProviderCollection providerCollection) {
        this.providerCollection = providerCollection;
    }

    @Override
    public void accept(ValidationEvent event) {
        if (event.getResult() == ValidationResult.ACCEPTED) {
            clearStatus();
        } else {
            setErrorStatus(event.getCharacterLimit(), event.getCurrentTextLength());
        }
    }

    private void clearStatus() {
        statusValue.setText("Okay!");
        statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), false);
    }

    private void setErrorStatus(CharacterLimit limit, int length) {
        String name = enumFormat(limit);
        String message = "'" + name + "' may be bigger than the limit, depending on e.g. variables."
                + "\n\nSize: " + length + " / " + limit.getMaxSize();

        statusValue.setText(message);

        statusValue.pseudoClassStateChanged(PseudoClass.getPseudoClass("error"), true);
    }

    private String enumFormat(Enum<?> enumEntry) {
        String name = enumEntry.name();
        StringBuilder output = new StringBuilder();
        boolean upperCase = true;

        for (char c : name.toCharArray()) {
            if (c == '_') {
                upperCase = true;
                output.append(" ");
                continue;
            }
            if (upperCase) {
                output.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                output.append(Character.toLowerCase(c));
            }
        }

        return output.toString();
    }

    private static class DebugPrintingGenerator implements Generator {

        @Override
        public String generate(ProviderCollection data) {
            String res = "";
            for (ProviderType providerType : ProviderType.values()) {
                switch (providerType.name().toUpperCase()) {
                    case "FIELDS": {
                        res += parseEmbed(data.get(providerType)) + " ";
                        break;
                    }
                    case "COLORED_BAR": {
                        if (!data.get(providerType).isEmpty()) {
                            String hex = Integer.toHexString(Integer.parseInt(data.get(providerType).get(0).toString().replace("0x", ""), 16));
                            res += "{Color:#" + hex.substring(0, hex.length() - 2) + "} ";
                        }
                        break;
                    }
                    case "AUTHOR_TEXT": {
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

                    case "AUTHOR_IMAGE": {
                        if (!data.get(providerType).isEmpty()) {
                            res += "{AuthorIcon:" + data.get(providerType).get(0).toString() + "} ";
                        }
                        break;
                    }

                    case "THUMBNAIL_IMAGE": {
                        if (!data.get(providerType).isEmpty()) {
                            res += "{Thumbnail:" + data.get(providerType).get(0).toString() + "} ";
                        }
                        break;
                    }

                    case "TITLE": {
                        if (!data.get(providerType).isEmpty()) {
                            List<TextWithUrl> list = data.get(providerType);
                            TextWithUrl title = list.get(0);
                            res += "{Title:" + title.getText() + "} ";
                            if (title.getUrl() != null)
                                res += "{TitleURL:" + title.getUrl() + "} ";
                        }
                        break;
                    }

                    case "DESCRIPTION": {
                        if (!data.get(providerType).isEmpty()) {
                            res += "{Description:" + data.get(providerType).get(0).toString() + "} ";
                        }
                        break;
                    }

                    case "IMAGE": {
                        if (!data.get(providerType).isEmpty()) {
                            res += "{Image:" + data.get(providerType).get(0).toString() + "} ";
                        }
                        break;
                    }
                    case "FOOTER_IMAGE": {
                        if (!data.get(providerType).isEmpty()) {
                            res += "{FooterIcon:" + data.get(providerType).get(0).toString() + "} ";
                        }
                        break;
                    }
                    case "FOOTER_TEXT": {
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
}
