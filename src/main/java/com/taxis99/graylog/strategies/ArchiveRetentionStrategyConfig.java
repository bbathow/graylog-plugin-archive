package com.taxis99.graylog.strategies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.graylog.autovalue.WithBeanGetter;
import org.graylog2.plugin.indexer.retention.RetentionStrategyConfig;

import javax.validation.constraints.Min;

@JsonAutoDetect
@AutoValue
@WithBeanGetter
public abstract class ArchiveRetentionStrategyConfig implements RetentionStrategyConfig {

    private static final int DEFAULT_MAX_NUMBER_OF_INDICES = 20;
    private static final String DEFAULT_REPO = "repo_all";

    @JsonProperty("max_number_of_indices")
    public abstract int maxNumberOfIndices();

    @JsonProperty("name_of_repository")
    public abstract String nameOfRepository();

    @JsonCreator
    public static ArchiveRetentionStrategyConfig create(@JsonProperty(TYPE_FIELD) String type,
                                                        @JsonProperty("max_number_of_indices") @Min(1) int maxNumberOfIndices,
                                                        @JsonProperty("name_of_repository") String nameOfRepository) {
        return new AutoValue_ArchiveRetentionStrategyConfig(type, maxNumberOfIndices, nameOfRepository);
    }

    @JsonCreator
    public static ArchiveRetentionStrategyConfig create(@JsonProperty("max_number_of_indices") @Min(1) int maxNumberOfIndices,
                                                        @JsonProperty("name_of_repository") String nameOfRepository) {
        return new AutoValue_ArchiveRetentionStrategyConfig(ArchiveRetentionStrategyConfig.class.getCanonicalName(), maxNumberOfIndices, nameOfRepository);
    }

    public static ArchiveRetentionStrategyConfig createDefault() {
        return create(DEFAULT_MAX_NUMBER_OF_INDICES, DEFAULT_REPO);
    }

}
