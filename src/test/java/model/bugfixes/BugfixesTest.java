package model.bugfixes;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import dto.LoadSchemaDTO;
import exception.InvalidFragmentException;
import model.normalization.Normalizer;
import model.normalization.RepositoryType;
import util.FileLoader;
import util.SchemaUtil;

import static org.junit.jupiter.api.Assertions.*;

public class BugfixesTest {

  private final static File BUGFIX_DIR = new File("src/test/resources/bugfixes");
  private final static LoadSchemaDTO config = new LoadSchemaDTO.Builder()
      .allowDistributedSchemas(true)
      .fetchSchemasOnline(true)
      .setRepType(RepositoryType.NORMAL)
      .build();

  @Test
  void refToNoPresentChildOfDefinitions() throws IOException {
    Normalizer normalizer =
        new Normalizer(new File(BUGFIX_DIR, "refToNoPresentChildOfDefinitions.json"), config);
    assertThrows(InvalidFragmentException.class, () -> normalizer.normalize());
  }

  @Test
  void refWithSpecialRegexLetters() throws IOException {
    final String fileName = "refWithSpecialRegexLetters.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR.getPath() + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithEscapedChars() throws IOException {
    final String fileName = "refWithEscapedChars.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idWithEmptyFragment() throws IOException {
    final String fileName = "idWithEmptyFragment.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithChangedBase() throws IOException {
    final String fileName = "refWithChangedBase.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithRelativeURI() throws IOException {
    final String fileName = "refWithRelativeURI.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithIllegalURI() throws IOException {
    final String fileName = "refWithIllegalURI.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToRootWithTrailingHash() throws IOException {
    final String fileName = "refToRootWithTrailingHash.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToChildOfChildOfDefinitions() throws IOException {
    final String fileName = "refToChildOfChildOfDefinitions.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithIdAndPointer() throws IOException {
    final String fileName = "refWithIdAndPointer.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idInEnum() throws IOException {
    final String fileName = "idInEnum.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToLastId() throws IOException {
    final String fileName = "refToLastId.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void idWithNoPath() throws IOException {
    final String fileName = "idWithNoPath.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void escapePercent() throws IOException {
    final String fileName = "escapePercent.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refToTopIdOfSchema() throws IOException {
    final String fileName = "refToTopIdOfSchema.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void refWithTrailingHash() throws IOException {
    final String fileName = "refWithTrailingHash.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
  }

  @Test
  void definitionKeyWithDots() throws IOException {
    final String fileName = "definitionKeyWithDots.json";
    JsonObject normalized = FileLoader.loadSchema(BUGFIX_DIR + "/Normalized_" + fileName);
    Normalizer normalizer = new Normalizer(new File(BUGFIX_DIR, fileName), config);
    assertEquals(normalized, normalizer.normalize());
    definitionKeysDoNotContainSlashesDotsOrStringDefinitions();
  }

  /**
   * Properties in {@code #/definitions} for all files in schema store
   * should not contain slashes, dots or the string "definitions".
   */
  private void definitionKeysDoNotContainSlashesDotsOrStringDefinitions() throws IOException {
    File store = new File("Store");
    for (File schema : Objects.requireNonNull(store.listFiles())) {
      String jsonStr = FileUtils.readFileToString(schema, StandardCharsets.UTF_8);
      JsonObject jsonObj = JsonParser.parseString(jsonStr).getAsJsonObject();
      JsonObject defs = SchemaUtil.getDefinitions(jsonObj);
      for (String key : defs.keySet()) {
        assertFalse(key.contains("/"));
        assertFalse(key.contains("."));
        assertFalse(key.contains("definitions"));
      }
    }
  }
  
  @AfterAll
  public static void cleanUp() throws IOException {
    new File("UriOfFiles.csv").delete();
    FileUtils.deleteDirectory(new File("Store"));
  }
}
