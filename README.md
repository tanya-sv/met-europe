# met-europe

The application "Met Europe" allows to browse and search through the department "European Paintings" of the The Metropolitan Museum of Art, using its open access data: https://github.com/metmuseum/openaccess.

The original CSV file was processed and all "European Paintings" department items extracted into a separate file. Since image url is not provided by the original data set, for each of these items following Public API endopint was called: https://collectionapi.metmuseum.org/public/collection/v1/objects/[objectID] and then saved in the same file (see [met_european_paintings.csv](/app/src/main/assets/met_european_paintings.csv)) together with some additional information (e.g. height, width). Here's the structure of this new CSV file:

`ObjectId;Department;Title;Artist;ArtistNationality;ObjectDate;ObjectDateBegin;ObjectDateEnd;Medium;Classification;Tags;ImageUrl;Height;Width`

The app allows the following:

- to scroll through all the items of the department, loading only small images;
- to filter the collection by era/data and artists nationality;
- mark favourite items and see them in a separate list;
- search by the tag (each item has a number of related tags) or artist name;
- load the full size image, zoom in/out of it and download it as an original file to the device;
- see more details about the object, that comes from the public API of the museum.

The Android technical approach: Kotlin, Coroutines, MVVM, Retrofit & OkHttp, RoomDB, Hilt. For image zooming: https://github.com/jsibbold/zoomage
