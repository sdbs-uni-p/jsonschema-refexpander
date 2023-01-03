package dto;

import model.normalization.RepositoryType;

import java.util.Map;

/**
 * DTO to store information about how schemas should be loaded. It is stored whether distributed
 * schemas should be allowed, whether references of the schema should be loaded online and from
 * which repository the schema is from.
 * 
 * @author Lukas Ellinger
 */
public class LoadSchemaDTO {
  private final boolean allowDistributedSchemas;
  private final boolean fetchSchemasOnline;
  private RepositoryType repType;
  private final Map<String, String> linksToPermalinks;

  public boolean isAllowDistributedSchemas() {
    return allowDistributedSchemas;
  }

  public boolean isFetchSchemasOnline() {
    return fetchSchemasOnline;
  }

  public RepositoryType getRepType() {
    return repType;
  }

  public void setRepType(RepositoryType repType) {
    this.repType = repType;
  }

  public boolean hasLinksToPermalinks() {
    return linksToPermalinks != null;
  }

  public String getPermalink(String link) {
    for (String key : linksToPermalinks.keySet()) {
      if (link.startsWith(key)) {
        return link.replace(key, linksToPermalinks.get(key));
      }
    }

    throw new RuntimeException(link + " has no permalink");
  }

  public boolean hasPermalink(String link) {
    for (String key : linksToPermalinks.keySet()) {
      if (link.startsWith(key)) {
        return true;
      }
    }
    return false;
  }

  public static LoadSchemaDTO of(boolean allowDistributedSchemas, boolean fetchSchemasOnline,
      RepositoryType repType, Map<String, String> linksToPermalinks) {
    return new LoadSchemaDTO(allowDistributedSchemas, fetchSchemasOnline, repType, linksToPermalinks);
  }

  private LoadSchemaDTO(boolean allowDistributedSchemas, boolean fetchSchemasOnline,
      RepositoryType repType, Map<String, String> linksToPermalinks) {
    this.allowDistributedSchemas = allowDistributedSchemas;
    this.fetchSchemasOnline = fetchSchemasOnline;
    this.repType = repType;
    this.linksToPermalinks = linksToPermalinks;
  }

  public static class Builder {
    private boolean allowDistributedSchemas;
    private boolean fetchSchemasOnline;
    private RepositoryType repType;
    private Map<String, String> linksToPermalinks;

    public Builder allowDistributedSchemas(boolean allowDistributedSchemas) {
      this.allowDistributedSchemas = allowDistributedSchemas;
      return this;
    }

    public Builder fetchSchemasOnline(boolean fetchSchemasOnline) {
      this.fetchSchemasOnline = fetchSchemasOnline;
      return this;
    }

    public Builder setRepType(RepositoryType repType) {
      this.repType = repType;
      return this;
    }

    public Builder addLinksToPermalinks(Map<String, String> linksToPermalinks) {
      this.linksToPermalinks = linksToPermalinks;
      return this;
    }

    public LoadSchemaDTO build() {
      return LoadSchemaDTO.of(allowDistributedSchemas, fetchSchemasOnline, repType, linksToPermalinks);
    }
  }
}
