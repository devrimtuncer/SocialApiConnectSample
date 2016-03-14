# SocialConnectionSample
A simple Android app which connects with social apis. Currently it supports Twitter, other social apis can be added later.
<br>
<b>Screens of this app:</b>
<ol>
<li>
Spash Screen: First screen of this app. Checks previous/saved logged in user information. If information exists, 
<i>List Screen</i> is opened with saved data, else user is redirected to <i>Login Screen</i> to complete social api connection.
</li>
<li>
Login Screen: User completes social api connections at this page. <i>List Screen</i> opens after connection completed.
</li>
<li>
List Screen: User's secure items (i.e. homeTimeline tweets) are fetched with social api client (i.e. twitter's official api). 
User can use "Pull to Refress" feature to refress items. User can click to list items to show detail at <i>Detail Screen</i>. 
There is a "LOGOUT" menu item to logout and clear saved user information.
</li>
<li>
Detail Screen: Item's detail is shown at this page.
</li>
</ol>
<p>
<b>Apis</b>
<p>
1) Crashlytics Api for crash reporting : <p>
```compile('com.crashlytics.sdk.android:crashlytics:2.5.5@aar') {
transitive = true;
}```
<p>
2) Twitter Api to connect with Twitter: <p>
```compile('com.twitter.sdk.android:twitter:1.13.0@aar') {
transitive = true;
}```
<p>
3) Android:<p>
`compile 'com.android.support:appcompat-v7:23.2.1'`<br/>
`compile 'com.android.support:support-v4:23.2.1'`<br/>
`compile 'com.android.support:recyclerview-v7:23.2.1'`<br/>
`compile 'com.android.support:design:23.2.1'`<br/>


<b>Extra:</b>
Application is developped to run when Developer Options --> Don't keep activities enabled.
