# SCRC Software checklist

This checklist has been developed by the Scottish Covid-19 Response Consortium and has been used to assess the software engineering aspects of "model readiness" for our 
six epidemiological models. This is intended to be part of a broader scheme to evaluate and describe suitability of model results to be used in decision making so 
this checklist covers only the software implementation and assumes that other documents cover questions about model validation, quality of science, 
data provenance and quality and policy readiness.

In order to use this checklist for the SCRC FAIR data pipeline components, three questions that apply only to software producing scientific results have been marked "models only" and for other software an N/A response can be given.

## Software details

### Model / software name

> javaDataPipeline

### Date

> 10/10/2021

### Version identifier

> 1.0.0-beta

## This assessment

### Filled in by

Name

> Bram Boskamp

Role on project: 

> Primary developer

Person identifier:

> github: B0SKAMP

### Overall statement

Do we have sufficient confidence in the correctness of the software to trust the results?

This is your overall judgement on the level of confidence based on all the aspects of the checklist. There is no formulaic way to arrive at this overall assessment based on the individual checklist answers but please explain how the measures in place combine to reach this level of confidence and make clear any caveats (eg applies for certain ways of using the software and not others).

> - [x] Yes
> - [ ] Yes, with caveats
> - [ ] No
>
> It does what it says on the tin.
> 

## Checklist

Please use a statement from this list: "Sufficiently addressed", "Some work remaining or caveats", or "Needs to be addressed" to begin each response.

Additionally, for each question please explain the situation and include any relevant links (eg tool dashboards, documentation). The sub bullet points are to make the scope of the question clear and should be covered if relevant but do not have to be answered individually.

### Can a run be repeated and reproduce exactly the same results? (models only)

- How is stochasticity handled?
- Is sufficient meta-data logged to enable a run to be reproduced: Is the exact code version recorded (and whether the repository was "clean"), including versions of dependent libraries (e.g. an environment.yml file or similar) along with all command line arguments and the content of any configuration files? 
- Is there up-to-date documentation which explains precisely how to run the code to reproduce existing results? 

> - [ ] Sufficiently addressed
> - [x] Some work remaining or caveats
> - [ ] Needs to be addressed
> - [ ] N/A
> 
> Distribution parameters stored in TOML parameter files are initialized with a RandomGenerator so that the user/model can draw samples from it. Work remaining: bring this RandomGenerator under the users control so that the user/modeller can handle the stochasticity.

### Are there appropriate tests?  (And are they automated?)

- Are there unit tests? What is covered?
- System and integration tests?  Automated model validation tests?
- Regression tests? (Which show whether changes to the code lead to changes in the output. Changes to the model will be expected to change the output, but many other changes, such as refactoring and adding new features, should not. Having these tests gives confidence that the code hasn't developed bugs due to unintentional changes.)
- Is there CI?
- Is everything you need to run the tests (including documentation) in the repository (or the data pipeline where appropriate)?

> - [ ] Sufficiently addressed
> - [x] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> Could do with some unit tests for the new api.* classes

### Are the scientific results of runs robust to different ways of running the code? (models only)

- Running on a different machine?
- With different number of processes?
- With different compilers and optimisation levels?
- Running in debug mode?

(We don't require bitwise identical results here, but the broad conclusions after looking at the results of the test case should be the same.) 

> - [ ] Sufficiently addressed
> - [ ] Some work remaining or caveats
> - [ ] Needs to be addressed
> - [x] N/A
> 
> Response here

### Has any sort of automated code checking been applied?

- For C++, this might just be the compiler output when run with "all warnings". It could also be more extensive static analysis. For other languages, it could be e.g. pylint, StaticLint.jl, etc.
- If there are possible issues reported by such a tool, have they all been either fixed or understood to not be important?

> - [x] Sufficiently addressed
> - [ ] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> JAVA compiler warnings have been addressed. Spotless has been used to keep the code in tidy google java format.

### Is the code clean, generally understandable and readable and written according to good software engineering principles?

- Is it modular?  Are the internal implementation details of one module hidden from other modules?
- Commented where necessary?
- Avoiding red flags such as very long functions, global variables, copy and pasted code, etc.?

> - [ ] Sufficiently addressed
> - [x] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> There are things that can be improved to make the code more easy to understand for developers or for users. 

### Is there sufficient documentation?

- Is there a readme?
- Does the code have user documentation?
- Does the code have developer documentation?
- Does the code have algorithm documentation? e.g. something that describes how the model is actually simulated, or inference is performed?
- Is all the documentation up to date? 

> - [ ] Sufficiently addressed
> - [x] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> There is a readme, there are javaDocs, there is user documentation, there is an example 'javaSimpleModel' to show how to use the
> library. All of this can still be improved, as can the developer documentation.

### Is there suitable collaboration infrastructure?

- Is the code in a version-controlled repository?
- Is there a license?
- Is an issue tracker used?
- Are there contribution guidelines?

> - [x] Sufficiently addressed
> - [ ] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> There are no contribution guidelines. Other than that it's all in place.

### Are software dependencies listed and of appropriate quality?

> - [x] Sufficiently addressed
> - [ ] Some work remaining or caveats
> - [ ] Needs to be addressed
> 
> Mostly obvious/standard dependencies.

### Is input and output data handled carefully? (Models only)

- Does the code use the data pipeline for all inputs and outputs?
- Is the code appropriately parameterized (i.e. have hard coded parameters been removed)?

> - [ ] Sufficiently addressed
> - [ ] Some work remaining or caveats
> - [ ] Needs to be addressed
> - [x] N/A
> 
> Response here


## Contributors and licence

Contributors: [to the software checklist template]
* Alys Brett
* James Cook
* Peter Fox
* Ian Hinder
* John Nonweiler
* Richard Reeve
* Robert Turner


Only a few points within the checklist are specific to the work of the SCRC collaboration or to epidemiology so the checklist could be readily adopted to modelling or analysis software in other domains. We welcome dialogue and suggestions for improvement via Github issues on this repository.

<a rel="license" href="http://creativecommons.org/licenses/by/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/4.0/">Creative Commons Attribution 4.0 International License</a>.


