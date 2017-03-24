### InAppTranslation
InAppTranslation automatically translates Android Widgets' texts at runtime by the power of Data Binding + Google Translate.  
Just by replacing `android:text` tag with `app:localizeText`, your app starts showing the UI in the user's local language.

### Install
```groovy
compile 'com.goldrushcomputing.inapptranslation:inapptranslation:0.9.0'
```


### Set Google Translate API Key
InAppTranslation uses Google Translate. Get the api key from [https://cloud.google.com/translate/](https://cloud.google.com/translate/)  

Set the key in local.properties file as below.
```groovy
inapptranslation.google_translate_apikey="Your_Google_Translate_API_Key"
```
(Don't share the local.properties file on GitHub.)

### Use
If your project is not ready for Data Binding, please do so by following  
[Data Binding Library - Android Developers](https://developer.android.com/topic/libraries/data-binding/index.html)  
In your layout xml file, put below `import` tag inside `<data>` section.  

```xml
<import type="com.goldrushcomputing.inapptranslation.InAppTranslation" />
```  

The in each widget in the layout, instead of using `android:text="Hello"`, use the tag below.
```java
app:localizeText="@{`Hello`}"
```


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

For ToggleButton and Switch, use
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
InAppTranslation sets the target language to the user's phone language.

### Specify Language
In case you don't use English as your base language, there is a chance that Google Translate doesn't recognize the source language properly.  
To avoid that, you can specify the base language by yourself.  
Add below code in your custom Application class's onCreate method.  
(You may want to change "ja" part of the code below if your base language is not Japanese.)
```java
InAppTranslation.setSourceLanguage("ja"); //translate from Japanese
```

### Road Map
* Provide a method to simply get translation data in Java code, in order to support localizing dynamic content such as news
* Cache translation results for better performance
* Better collaborate with strings.xml - use strings.xml if translation already exists.



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
