# Tripify
Tripify is a itinerary trip planner for users to easily plan their holiday trips and activities around the world.<br>

**Playstore Link:**
https://play.google.com/store/apps/details?id=sg.edu.np.mad.assignment

**Group members:**
- S10208470A | Shanice Yeong
- S10204757E | Faris Zharfan BAG
- S10208045C | Xu Zhihong
- S10202752B | Sean Tan Yi Xun
- S10207999D | Nur Harith Mirza


## Features
- Account creation and customization
- Creation of trip and activities
- Currency conversion calculator

## Contributions of group members
### S10208470A | Shanice Yeong
**Creation of trips** <br>
One of the app's main features; without it, it would not be a planning app. Using Recyclerview and View Holder, created trips are then displayed on the Homescreen.

**Layout / UI design**<br>
Using Figma, I created a UI design prototype. Making sure that the design is clean and modern, while also being simple enough to translate into code. Also responsible of the most of of the layout constraints in Android Studio.

*Figma link:* https://www.figma.com/file/pGfOdaeSzKktCcty6fp0S3/Trip-planning?node-id=0%3A1

### S10204757E | Faris Zharfan BAG
**Creation of profile screen** <br>
To allow for user profile setup and identification, very much possibly needed for future implementations. It allows for storage of user's information such as name and home country and also a profile picture for the user. 

**Set up of firebase storage** <br>
Setting up rules to ensure only authenticated users are allowed to read and write data from the cloud storage. Also set up the standardised path that future implementations will be using when storing of photos and videos are needed

**Set up of Budget and Flight Details** <br>
Travelling is an essentially part of some people's lives. They simple cannot just stay in their home country. However, it is quite an expensive hobby. As such, some people are quite conscious of their expenses. Hence, it would be useful for them to keep track of their expenditures and as an add on keep track of the flights they have on that trip



### S10208045C | Xu Zhihong
**User Authentication/Log-in/Forget password** <br>
Enabling user to register for an account and authenticate in order to save all of their data in the cloud (Firebase). If a user forgets their password, they can choose to change it by selecting the forget password option. 

**User Authentication/Log-in/Forget password** <br>
With a Good Travel application, there must be a translation. This translation applciation allow users to translate remotely in many different language(currently only have 12 languages). User can type or speak using voice recognition to translate into other language. The application uitilized FIrebase machine learning language processor to load the language. 

### S10202752B | Sean Tan Yi Xun
**Created Currency Converter** <br>
One useful feature of the currency converter is that it has the most frequently used currencies in it and can be converted interchangeably, enabling you to simplify your planning by accessing simple but yet important things such as exchange rates.  Unfortunately as APIs for live updated currencies cost a monthly fee or were not updated I was not able to implement one and had to instead hard code it out. I also added another tab at the bottom of the screen at the home page for ease of navigation to the currency converter. 

**Set up of weather features** <br>
Added weather feature where it shows the current weather of where you are, as well as the weather of some cities. Using a weather API I was able to take the latitude and longitude of my desired location and it would feedback the weather data there. 

For currency converter the number input was changed to only show numbers and no longer showed a keyboard

### S10207999D | Nur Harith Mirza
**Creation of Activities** <br>
One of the main feature of the app - Allows users to add travel Itineraries to their travels. Using Recyclerview and View Holder, created activities are then displayed on the Activities Page. Allow users to add the Itinerary name, location (which is autofilled with google autocomplete places api), time of event. Helps users to plan their trips properly.
