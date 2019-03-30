package org.janelia.flyem.neuprintprocedures.proofreading;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.janelia.flyem.neuprint.model.Soma;
import org.janelia.flyem.neuprint.model.Synapse;

import java.util.Set;

public class NeuronAddition {

    @SerializedName("Id")
    private Long bodyId;

    @SerializedName("Size")
    private Long size;

    @SerializedName("MutationUUID")
    private String mutationUuid;

    @SerializedName("MutationID")
    private Long mutationId;

    @SerializedName("Status")
    private String status; // optional

    @SerializedName("Name")
    private String name; // optional

    @SerializedName("Soma")
    private Soma soma; // optional

    @SerializedName("CurrentSynapses")
    private Set<Synapse> currentSynapses;

    public Long getBodyId() {
        return bodyId;
    }

    public Long getSize() {
        return size;
    }

    public String getMutationUuid() {
        return mutationUuid;
    }

    public Long getMutationId() {
        return mutationId;
    }

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public Soma getSoma() {
        return soma;
    }

    public Set<Synapse> getCurrentSynapses() {
        return currentSynapses;
    }

    public void setToInitialMutationId() {
        this.mutationId = 0L;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this, NeuronAddition.class);
    }

    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (this == o) {
            isEqual = true;
        } else if (o instanceof NeuronAddition) {
            final NeuronAddition that = (NeuronAddition) o;
            isEqual = this.mutationUuid.equals(that.mutationUuid)
                    && this.mutationId.equals(that.mutationId);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.mutationUuid.hashCode();
        result = 31 * result + this.mutationId.hashCode();
        return result;
    }
}
