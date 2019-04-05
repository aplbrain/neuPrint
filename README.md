[![Build Status](https://travis-ci.org/connectome-neuprint/neuPrint.svg?branch=master)](https://travis-ci.org/connectome-neuprint/neuPrint) 
[![GitHub issues](https://img.shields.io/github/issues/connectome-neuprint/neuPrint.svg)](https://GitHub.com/connectome-neuprint/neuPrint/issues/)


# neuPrint
A blueprint of the brain. A set of tools for loading and analyzing connectome data into a Neo4j database. Analyze and explore connectome data stored in Neo4j using the neuPrint ecosystem: [neuPrintHTTP](https://github.com/connectome-neuprint/neuPrintHTTP), [neuPrintExplorer](https://github.com/connectome-neuprint/neuPrintExplorer), [Python API](https://github.com/connectome-neuprint/neuprint-python). 

[Javadocs](https://connectome-neuprint.github.io/neuPrint/)

## Requirements
* Neo4j version 3.5.3
* [apoc version 3.5.0.1](https://github.com/neo4j-contrib/neo4j-apoc-procedures/releases/tag/3.5.0.1)<sup>1</sup>
* **Optional** neuprint-load-procedures.jar OR neuprint-procedures.jar (found in `executables` directory) <sup>2</sup>
    
1. Note that to install plugins in neo4j, the .jar files must be copied into the plugins directory of your neo4j database. [This may be helpful.](https://community.neo4j.com/t/how-can-i-install-apoc-library-for-neo4j-version-3-4-6-edition-community/1495)
2. One of these is required for loading. Triggers in neuprint-procedures.jar may slow down the load, so neuprint-load-procedures.jar is recommended. neuprint-procedures.jar contains custom stored procedures and functions for use with the rest of the neuPrint ecosystem. If using with neuPrintHTTP and neuPrintExplorer, install neuprint-procedures after loading the data.


## Example data

* mb6 : from ["A connectome of a learning and memory center in the adult Drosophila brain"](https://elifesciences.org/articles/26975) (Takemura, et al. 2017)

* fib25 : from ["Synaptic circuits and their variations within different columns in the visual system of Drosophila"](https://www.pnas.org/content/112/44/13711) (Takemura, et al. 2015)

## Load mb6 connectome data into Neo4j

1. After cloning the repository, set uri, user, and password in the example.properties file to match the those of the target database. You can also change the batch size for database transactions in this file (default is 100). Unzip mb6_neo4j_inputs.zip.  

2. Check that you're using the correct version of Neo4j (see Requirements) and that apoc and neuprint-load-procedures are installed. 

3. Run the following on the command line:
```console
$ java -jar executables/neuprint.jar --dbProperties=example.properties --datasetLabel=mb6 --datasetLabel=mb6 --synapseJson=mb6_neo4j_inputs/mb6_new_spec_Synapses.json --connectionsJson=mb6_neo4j_inputs/mb6_new_spec_Synaptic_Connections.json --neuronJson=mb6_neo4j_inputs/mb6_new_spec_Neurons.json --skeletonDirectory=mb6_neo4j_inputs/mb6_skeletons --metaInfoJson=meta-data/mb6_meta_data.json
```

## Load your own connectome data into Neo4j using neuPrint

Follow these [input specifications](jsonspecs.md) to create your own neurons.json, synapses.json, and skeleton files. To create a database on your computer, use [Neo4j Desktop](https://neo4j.com/download/?ref=product).

```console
$ java -jar executables/neuprint.jar --help
  
Usage: java -jar neuprint.jar [options]
  Options:
    --addClusterNames
      Indicates that cluster names should be added to Neuron nodes. (true by 
      default) 
      Default: true
    --addConnectionSetRoiInfoAndWeightHP
      Indicates that an roiInfo property should be added to each ConnectionSet 
      and that the weightHP property should be added to all ConnectionSets 
      (true by default).
      Default: true
    --connectionsJson
      Path to JSON file containing synaptic connections.
    --dataModelVersion
      Data model version (required)
      Default: 1.0
  * --datasetLabel
      Dataset value for all nodes (required)
  * --dbProperties
      Properties file containing database information (required)
    --help

    --metaInfoJson
      JSON file containing meta information for dataset
    --neuronJson
      JSON file containing neuron data to import
    --neuronThreshold
      Integer indicating the number of synaptic densities (>=neuronThreshold/5 
      pre OR >=neuronThreshold post) a neuron should have to be given the 
      label of :Neuron (all have the :Segment label by default).
      Default: 10
    --postHPThreshold
      Confidence threshold to distinguish high-precision postsynaptic 
      densities (default is 0.0)
      Default: 0.0
    --preHPThreshold
      Confidence threshold to distinguish high-precision presynaptic densities 
      (default is 0.0)
      Default: 0.0
    --skeletonDirectory
      Path to directory containing skeleton files for this dataset
    --synapseJson
      JSON file containing body synapse data to import

```
## neuPrint Property Graph Model

[Model details](pgmspecs.md)

## Developer Instructions

`mvn test` will run all tests, `mvn package` will run all tests and package the .jar file. Running `mvn verify` will copy .jar files to the `executables` directory (`package` places .jar files in the `target` directory). Versioning must be done manually in the pom.xml file.

## neuPrint Custom Procedures and Functions
These are found in the neuprint-procedures.jar file:
* [Graph Update API](graphupdateAPI.md)
* Analysis Procedures (docs coming soon)
      

