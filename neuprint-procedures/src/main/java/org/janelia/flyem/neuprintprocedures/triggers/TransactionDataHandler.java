package org.janelia.flyem.neuprintprocedures.triggers;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.LabelEntry;
import org.neo4j.graphdb.event.PropertyEntry;
import org.neo4j.graphdb.event.TransactionData;

import java.util.HashSet;
import java.util.Set;

class TransactionDataHandler {

    // nodes with unique ids
    private static final String DATA_MODEL = "DataModel";
    private static final String NEURON = "Neuron";
    private static final String META = "Meta";
    private static final String SKELETON = "Skeleton";
    private static final String SKEL_NODE = "SkelNode";
    private static final String SYNAPSE = "Synapse";
    private static final String SYNAPSE_SET = "SynapseSet";
    // properties
    private static final String TIME_STAMP = "timeStamp";

    private TransactionData transactionData;
    private Set<Node> nodesForTimeStamping;
    private boolean shouldMetaNodeSynapseCountsBeUpdated;

    TransactionDataHandler(TransactionData transactionData) {
        this.transactionData = transactionData;
        this.nodesForTimeStamping = new HashSet<>();
    }

    Set<Node> getNodesForTimeStamping() {

        shouldMetaNodeSynapseCountsBeUpdated = false;

        for (Node node : transactionData.createdNodes()) {
            if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                this.nodesForTimeStamping.add(node);
                //System.out.println("node created: " + node);
            }
            if (node.hasLabel(Label.label(SYNAPSE)) && !transactionData.isDeleted(node)) {
                shouldMetaNodeSynapseCountsBeUpdated = true;
            }
        }

        for (LabelEntry labelEntry : transactionData.assignedLabels()) {
            Node node = labelEntry.node();
            if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                this.nodesForTimeStamping.add(node);
                //System.out.println("label entry assigned: " + labelEntry);
            }
            if (node.hasLabel(Label.label(SYNAPSE)) && !transactionData.isDeleted(node)) {
                shouldMetaNodeSynapseCountsBeUpdated = true;
            }
        }

        for (LabelEntry labelEntry : transactionData.removedLabels()) {
            Node node = labelEntry.node();
            if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                this.nodesForTimeStamping.add(node);
                //System.out.println("label entry removed: " + labelEntry);
            }
            if (node.hasLabel(Label.label(SYNAPSE)) && !transactionData.isDeleted(node)) {
                shouldMetaNodeSynapseCountsBeUpdated = true;
            }
        }

        for (PropertyEntry<Node> propertyEntry : transactionData.assignedNodeProperties()) {
            if (!propertyEntry.key().equals(TIME_STAMP)) {
                Node node = propertyEntry.entity();
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("node properties assigned: " + propertyEntry);
                }
            }
        }

        for (PropertyEntry<Node> propertyEntry : transactionData.removedNodeProperties()) {
            if (!propertyEntry.key().equals(TIME_STAMP)) {
                Node node = propertyEntry.entity();
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("node properties removed: " + propertyEntry);
                }
            }
        }

        for (PropertyEntry<Relationship> propertyEntry : transactionData.assignedRelationshipProperties()) {
            Relationship relationship = propertyEntry.entity();
            Node[] nodes = relationship.getNodes();
            for (Node node : nodes) {
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("relationship properties added for: " + node);
                }
            }
        }

        for (PropertyEntry<Relationship> propertyEntry : transactionData.removedRelationshipProperties()) {
            Relationship relationship = propertyEntry.entity();
            Node[] nodes = relationship.getNodes();
            for (Node node : nodes) {
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("relationship properties removed for: " + node);
                }
            }
        }

        for (Relationship relationship : transactionData.createdRelationships()) {
            Node[] nodes = relationship.getNodes();
            for (Node node : nodes) {
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("relationship created for: " + node);
                }
            }
        }

        for (Relationship relationship : transactionData.deletedRelationships()) {
            Node[] nodes = relationship.getNodes();
            for (Node node : nodes) {
                if (!node.hasLabel(Label.label(META)) && !transactionData.isDeleted(node)) {
                    this.nodesForTimeStamping.add(node);
                    //System.out.println("relationship deleted for: " + node);
                }
            }
        }

        return nodesForTimeStamping;

    }

    boolean shouldTimeStampAndUpdateMetaNodeTimeStamp() {
        //if time stamping, means a significant change happened during transaction that wasn't the addition of a time stamp or alteration of the meta node itself
        return (this.nodesForTimeStamping.size() > 0);
    }

    boolean getShouldMetaNodeSynapseCountsBeUpdated() {
        return this.shouldMetaNodeSynapseCountsBeUpdated;
    }

}