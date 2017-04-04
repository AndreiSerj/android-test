1. The app based on MVP pattern (Mosby)
2. The app uses Realm.io as a persistence layer


How to:
1. On the very first start the app reads locations.json, parses it and populates the realm storage with default locations
2. There are two tabs on the main screen: List and Map. Both tabs display the list of default and new created (long tap on the map) locations.
3. Tap on the list item as well as on the map's info toast will redirect to the details screen.
4. Details screen shows the selected location's attributes and allows to fill/edit a description field.
5. Long tap on the map allows user to create ad add new location on the map. This new location is saved in persisted storage