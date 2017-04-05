1. The app based on MVP pattern (Mosby)
2. The app uses Realm.io as a persistence layer
3. List of locations is sorted by distance (relativly to Sydney)
4. Map: Default markers are RED, custom are BLUE.

How to:
1. On the very first start a splash screen appears and the app reads the locations.json, parses it and populates the realm-based storage with the default locations.
This operation performs only once on the very first run. Then, a main screen appears.

2. There are two tabs on the main screen: List and Map. 
Both tabs display the list of default and new added locations.
New custom location is created by long tap on the map.

3. Tap on the list item as well as on the map's info toast will redirect to the details screen.

4. Details screen shows the selected location's attributes and allows user to add/edit a description field.

5. Long tap on the map allows user to create and add new location on the map. 
This new location is saved in persisted storage.

6. Tap on location marker shows the info toast which is clickable. 
Click on info toast redirect to the details screen.

Task execution time: 6+ h (dev) + 1+ h(tests)