&larr; [Previous step: Staging the release using Docker image](./step8-staging-the-release.md)

# Further exercises and improvements

Here we gathered the list of possible changes and improvements in the project.
You can implement it independently, by your own, and even propose a pull request into the main code base 
if you think change worth it.

Client-side changes proposed:
1. Apply validation for the `PhoneNumber` field on the 'Add Customer' form.
2. Configure 'Get Info', 'Check-out', 'Check-in', and 'Delete' actions for `Customers` and `Correspondence`. 
3. Implement a configuration console pane for managing `States` and `Cities`.

Server-side changes proposed:
1. Download CSV files directly from the GitHub instead of putting it into the resources.
2. Use a database (e.g. embedded H2) instead of CSV files. However, you can initiate a data using the CSV.
3. Sort `Cities` alphabetically.
4. Implement a full CRUD API for `States` and `Cities`.

Thank you! 