### InAppTranslation
InAppTranslation automatically translate Android Widgets' texts at runtime by the power of Data Binding + Google Translate.
Just by replacing `android:text` tag with `app:localizeText`, your app starts showing the UI in the user's local language.

### Install
```groovy
compile 'com.goldrushcomputing.inapptranslation:inapptranslation:0.9.0'
```

### Set Google Translate API Key
InAppTranslation uses Google Translate so that you have to obtain Google Translate API key.  
Set the key in local.properties file as below.
```groovy
inapptranslation.google_translate_apikey="Your_Google_Translate_API_Key"
```
And don't share the local.properties file on GitHub ;)

### Use
In you layout xml file, instead of using `android:text="Hello"`, use below
```java
app:localizeText="@{`Hello`}"
```
That's it!

Here is an example of TextView using the tag.
```java
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:localizeText="@{`Hello`}"/>
```

You can also specify string resource.
```java
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:localizeText="@{@string/start}"/>
```
`app:localizeText` tag supports
* TextView
* Button
* CheckBox
* RadioButton
* CheckedTextView
* EditText

For ToggleButton and Switch there are two tags. 
```java
app:localizeTextOff="@{`Off`}"
app:localizeTextOn="@{`On`}"
```
which works like `android:textOff` and `android:textOn`.

For EditText's hint text, you can use below hint tag
```java
app:localizeHint="@{`Type your name`}"
```

### How Translation Works
InAppTranslation library uses Google Translate api to translate texts.  
The source language is automatically inferred by Google Translate.
The target language is the user's phone language.

### Specify Language
In case you don't use English as your base language, there is a chance that Google Translate doesn't recognize your source language propely.  
To avoid that, you can specify the base language.  
Add below code in your custom Application class's onCreate method.  
(You can change "ja" part if your base language is not English.)
```java
InAppTranslation.setSourceLanguage("ja"); //translate from Japanese
```

### Road Map
* Provide a method to simply get translation data in Java code to support localizing dynamic content as news
* Cache translation result for better performance
* Better collaborate with strings.xml - User strings.xml if translation already exists. 



### Proguard
Would love to have proguard pull request for consumer proguard implementation

### License
<pre>
MIT License

Copyright (c) 2017 Takamitsu Mizutori

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
</pre>
