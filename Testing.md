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
- 