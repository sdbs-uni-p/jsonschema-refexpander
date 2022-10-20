package util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.StoreException;

/**
 * Is used to store schemas in a directory. A uri is safed in a csv-File with the associate file
 * name.
 * 
 * @author Lukas Ellinger
 */
public class Store {
  private static int counter = 0;
  private static File dir = new File("Store");
  private static File csv = new File("UriOfFiles.csv");

  static {
    try {
      if (csv.exists()) {
        List<CSVRecord> records = CSVUtil.loadCSV(csv, ',', false);
        
        if (records.size() > 0) {
          CSVRecord lastRecord = records.get(records.size() - 1);
          String name = lastRecord.get(0);
          
          // replaces all except numbers
          counter = Integer.parseInt(name.replaceAll("[^\\d]", "")) + 1;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Stores <code>object</code> in <code>dir</code> and makes an entry in <code>csv</code> with its
   * filename and <code>uri</code>.
   * 
   * @param object to be stored.
   * @param uri to associate <code>object</code> with.
   * @throws IOException if it cannot be stored or if entry to <code>csv</code> cannot be made.
   */
  public static void storeSchema(JsonObject object, URI uri) throws IOException {
    if (!dir.exists()) {
      dir.mkdir();
    }

    File file = new File(dir, "js_" + counter + ".json");
    SchemaUtil.writeJsonToFile(convertRefPaths(object), file);
    String[] line = {file.getName(), uri.toString()};
    CSVUtil.writeToCSV(csv, line);
    counter++;
  }

  /**
   * For each keyword in definitions object kills slashes and dots and replaces the substring "definitions.
   * Adjusts $ref value.
   *
   * @param object The JSON object to convert its references.
   * @return A JSON object with converted references.
   */
  private static JsonObject convertRefPaths(JsonObject object) {
    if (object.has("definitions")) {
      JsonObject defs = object.get("definitions").getAsJsonObject();
      JsonObject newDefs = new JsonObject();

      for (String key : defs.keySet()) {
        String newKey = key.replace("/", "_").replace(".", "_").replace("definitions", "defs");
        newDefs.add(newKey, defs.get(key));
      }

      object.add("definitions", newDefs);
      return findAndReplaceRefPathsInElement(object).getAsJsonObject();
    }

    return object;
  }

  private static JsonElement findAndReplaceRefPathsInElement(JsonElement json) {
    if (json.isJsonArray()) {
      JsonArray array = new JsonArray();

      for (JsonElement elem : json.getAsJsonArray()) {
        array.add(findAndReplaceRefPathsInElement(elem));
      }

      return array;
    }

    if (json.isJsonObject()) {
      JsonObject object = new JsonObject();
      JsonObject obj = json.getAsJsonObject();

      for (String key : obj.keySet()) {
        if (key.equals("$ref")) {
          String value;
          try {
            value = obj.get(key).getAsString();

            if (value.startsWith("#/definitions/")) {
              String suffix = value.substring("#/definitions/".length());
              suffix = suffix.replace(".", "_").replace("definitions", "defs");
              value = "#/definitions/" + suffix;
            }

            object.addProperty(key, value);
          } catch (UnsupportedOperationException e) {
            object.add(key, findAndReplaceRefPathsInElement(obj.get(key)));
          }
        } else {
          object.add(key, findAndReplaceRefPathsInElement(obj.get(key)));
        }
      }

      return object;
    }

    return json;
  }

  public static JsonObject getSchema(URI uri) throws StoreException, IOException {
    if (csv.exists()) {
      List<CSVRecord> records = CSVUtil.loadCSV(csv, ',', false);

      for (CSVRecord record : records) {
        if (record.get(1).equals(uri.toString())) {
          File file = new File(dir, record.get(0));
          return new Gson().fromJson(FileUtils.readFileToString(file, "UTF-8"), JsonObject.class);
        }
      }

      throw new StoreException("No file associated with " + uri + " found in store");
    } else {
      throw new StoreException(csv.getName() + " does not exist");
    }
  }
}
