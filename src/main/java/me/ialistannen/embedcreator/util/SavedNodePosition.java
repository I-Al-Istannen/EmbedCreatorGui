package me.ialistannen.embedcreator.util;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Saves node positions
 */
public class SavedNodePosition {

  private int childIndex;
  private Integer gridRow;
  private Integer gridColumn;

  /**
   * Restores the {@link Node}'s position <em>and adds it to the paren</em>.
   *
   * @param target The {@link Node} to restore it to
   * @param parent The parent {@link Pane}, to add it to.
   */
  public void restore(Node target, Pane parent) {
    parent.getChildren().add(childIndex, target);
    if (gridRow != null) {
      GridPane.setRowIndex(target, gridRow);
    }
    if (gridColumn != null) {
      GridPane.setColumnIndex(target, gridColumn);
    }
  }

  /**
   * Saves the position for a {@link Node}.
   *
   * @param node The {@link Node} to save it for
   * @return The {@link SavedNodePosition}
   */
  public static SavedNodePosition of(Node node) {
    SavedNodePosition savedNodePosition = new SavedNodePosition();
    if (node.getParent() != null) {
      savedNodePosition.childIndex = node.getParent().getChildrenUnmodifiable().indexOf(node);
    }
    savedNodePosition.gridColumn = GridPane.getColumnIndex(node);
    savedNodePosition.gridRow = GridPane.getRowIndex(node);

    return savedNodePosition;
  }

}
