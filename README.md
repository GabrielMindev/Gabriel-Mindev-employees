An application that identifies the pair of employees who have worked
together on common projects for the longest period of time. The input 
data is loaded to the program from a CSV file which consists of 
data in the following format:
EmpID, ProjectID, DateFrom, DateTo
-DateTo can be NULL, equivalent to today
The application has an UI which allows the user to pick up a file from 
the file system and, after selecting it, all common projects of the pair 
are displayed in datagrid with the following columns:
Employee ID #1, Employee ID #2, Project ID, Days worked
