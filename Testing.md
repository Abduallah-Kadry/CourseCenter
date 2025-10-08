- testing starts from the requirements gatherings (testing logic)
- req gather, planning, analysis
- Arrange a scenario, Act or do the think you want in that scenario then assert that what you did actually worked
    - you may arrange the same scenario but apply different acts then assert these acts all works
- Test the happy path (make sure that things will go right if everything is alright) -> core function like student
  register
- Test the sad path (make sure that errors are thrown when planned or expected problems happen) -> core error thrown
  like throw error when student couldn't register and throw the proper exception
- Test Business Rules like student cant enroll in more than 3 courses at the same time

- Ask what are the input boundaries like is there Nulls, empty strings, negative numbers, wrong dates input
- Ask what are the output expectations of each scenario (should be discussed where requirements in mind)
- Ask what are the side effects of each method (is there notification, deleting a course will delete sessions of that
  course? updating score recalculate analysis)


when starting it is best to start where to put your files 

well first we should deal with the exception throw mechanism 
- what is checked and unchecked
- what is the best structure for exception for the application
- when to use custom exception and when to use native ones
- made `ApiRespond` class that could be used for both exceptions and normal respond and friendly to apis and frontend (doen't know if is bad for security or not)
- when outer service throws some exceptions out of your custom exception how to handle them ? (what is the best practice for this) (donknow yet)
 


   ┌─────────────────────────────────────┐
1. │   What are you testing?             │
   └─────────────────┬───────────────────┘
                    │
          ┌─────────┴─────────┐
          │                   │
      ONE CLASS          MULTIPLE CLASSES
          │                   │
          ▼                   ▼
     UNIT TEST         INTEGRATION TEST
     (Same package)      (/integration/)


2. Database involved?
├── YES, real database
│   └── Put in: /repository/ or /integration/
│
└── NO, mocked
    └── Put in: /service/ or /controller/


3. Layers involved:
├── 1 layer (Service only)
│   └── /service/ServiceTest.java
│
├── 2 layers (Controller + Service)
│   └── /controller/ControllerTest.java (with mocked service)
│
└── 3+ layers (Full flow)
    └── /integration/FeatureFlowTest.java


