# [TableFixHeaders] (https://github.com/InQBarna/TableFixHeaders)-Wrapper
## What is it?
[TableFixHeaders] (https://github.com/InQBarna/TableFixHeaders) is an Android widget to display tables with headers.

**TableFixHeaders-Wrapper** is a [wrapper](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/library/src/main/java/miguelbcr/ui/tableFixHeadesWrapper/TableFixHeaderAdapter.java) for [TableFixHeaders] (https://github.com/InQBarna/TableFixHeaders) with the goal of separate the views from the adapter and keep the code cleaner

## Features

* Adapter class to fill the table in an easy way.

* Own scroll.

* Impulse scroll.

* Recycling of views.

* Shadows when there is more info to scroll.

* Click and LongClick listeners for each cell view of the table

* Required minimum API level: 5

##Setup

Add the JitPack repository in your build.gradle (top level module):
```gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And add next dependencies in the build.gradle of the module:
```gradle
dependencies {
    compile "com.github.miguelbcr:TableFixHeaders-Wrapper:0.2.0"
}
```

##Usage

### **1. Creating the table in your layout XML**

As usual, by the [TableFixHeaders] (https://github.com/InQBarna/TableFixHeaders) way:

```xml
    <com.inqbarna.tablefixheaders.TableFixHeaders
        android:id="@+id/tablefixheaders"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```

### **2. Creating the table views**

There are at most 5 types of views. Imagine a matrix of **R**ows x **C**olumns. See [screenshots section](#screenshotssection):
* First header (FH): cell R=0 C=0
* Rest of headers (H): cells R=0 C=[1..C-1]
* First body (FB): cells C=0 R=[1..R-1] `&& !isSection()`
* Body (B): cells R=[1..R-1] C=[1..C-1] `&& !isSection()`
* Section: It is a row separator and could be any row in R=[1..R-1] where `isSection() == true` (See method in [TableFixHeaderAdapter](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/library/src/main/java/miguelbcr/ui/tableFixHeadesWrapper/TableFixHeaderAdapter.java))
 
Each view determines its role by the interface/s defined.

For example, if you want to create a unique view for all the table you can do something like this:

```java
public class BasicCellViewGroup extends FrameLayout
        implements
        TableFixHeaderAdapter.FirstHeaderBinder<String>,
        TableFixHeaderAdapter.HeaderBinder<String>,
        TableFixHeaderAdapter.FirstBodyBinder<List<String>>,
        TableFixHeaderAdapter.BodyBinder<List<String>>,
        TableFixHeaderAdapter.SectionBinder<List<String>> {

    public TextView textView;

    public BasicCellViewGroup(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(android.R.layout.test_list_item, this, true);
        textView = (TextView) findViewById(android.R.id.text1);
    }


    @Override
    public void bindFirstHeader(String headerName) {
        textView.setText(headerName);
    }

    @Override
    public void bindHeader(String headerName, int column) {
        textView.setText(headerName);
    }

    @Override
    public void bindFirstBody(List<String> items, int row) {
        textView.setText("Row " + (row + 1));
    }

    @Override
    public void bindBody(List<String> items, int row, int column) {
        textView.setText(items.get(column + 1));
    }

    @Override
    public void bindSection(List<String> item, int row, int column) {
        textView.setText(column == 0 ? "Section:" + (row + 1) : "");
    }
}
```

but if we want to represent a view only for the first header we could do something like this:

```java
public class FirstHeaderCellViewGroup extends FrameLayout
        implements
        TableFixHeaderAdapter.FirstHeaderBinder<String> {

    public TextView textView;

    public BasicCellViewGroup(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(android.R.layout.test_list_item, this, true);
        textView = (TextView) findViewById(android.R.id.text1);
    }


    @Override
    public void bindFirstHeader(String headerName) {
        textView.setText(headerName);
    }
}
```

and so on for the rest of the table views, depending on the intefaces implementend by the view.

The interfaces implemented receives a param which is the data type used to populate that view.

**Note:** By library issues, the interfaces `TableFixHeaderAdapter.FirstBodyBinder<T>`, `TableFixHeaderAdapter.BodyBinder<T>` and `TableFixHeaderAdapter.SectionBinder<T>` have to receive the same data type, otherwise a compilation error will appear if it does not match with the adapter signature.


### **3. Creating the adapter**

The next step is create your table adapter that inherits from [TableFixHeaderAdapter](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/library/src/main/java/miguelbcr/ui/tableFixHeadesWrapper/TableFixHeaderAdapter.java), where you define the columns widths, the rows heights and table views created prevously. It receives as params the views and the data type of each one of the views created previously.

```java
public class BasicTableFixHeaderAdapter extends TableFixHeaderAdapter<
        String, BasicCellViewGroup,
        String, BasicCellViewGroup,
        List<String>,
        BasicCellViewGroup,
        BasicCellViewGroup,
        BasicCellViewGroup> {

    private Context context;

    public BasicTableFixHeaderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected BasicCellViewGroup inflateFirstHeader() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateHeader() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateFirstBody() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateBody() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateSection() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected List<Integer> getHeaderWidths() {
        List<Integer> headerWidths = new ArrayList<>();

        // First header
        headerWidths.add((int)  context.getResources().getDimension(R.dimen._150dp));

        for (int i = 0; i < 20; i++)
            headerWidths.add((int)  context.getResources().getDimension(R.dimen._100dp));

        return headerWidths;
    }

    @Override
    protected int getHeaderHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getSectionHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getBodyHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected boolean isSection(List<List<String>> items, int row) {
        return false;
    }
}
```


### **4. Putting all together**

For example, in your activity:

```java
import com.inqbarna.tablefixheaders.TableFixHeaders;

public class MainActivity extends AppCompatActivity {
    private TableFixHeaders tablefixheaders

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        tablefixheaders = (TableFixHeaders) findViewById(R.id.tablefixheaders);
        tablefixheaders.setAdapter(getAdapter());
	}

    public BaseTableAdapter getAdapter() {
        BasicTableFixHeaderAdapter adapter = new BasicTableFixHeaderAdapter(this);
        List<List<String>> body = getBody();

        adapter.setFirstHeader("BASIC");
        adapter.setHeader(getHeader());
        adapter.setFirstBody(body);
        adapter.setBody(body);
        adapter.setSection(body);

        return adapter;
    }

    private List<String> getHeader() {
        List<String> header = new ArrayList<>();

        for (int i = 0; i < 20; i++)
            header.add("H " + (i + 1));

        return header;
    }

    private List<List<String>> getBody() {
        List<List<String>> rows = new ArrayList<>();

        for (int row = 0; row < 100; row++) {
            List<String>  cols = new ArrayList<>();

            for (int col = 0; col < 30; col++)
                cols.add("Col " + col);

            rows.add(cols);
        }

        return rows;
    }
}
```

## <a name="screenshotssection"></a>Screenshots
![Screenshot 1](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/web/basic.png)

![Screenshot 2](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/web/original.png)

![Screenshot 3](https://github.com/miguelbcr/TableFixHeaders-Wrapper/blob/master/web/original_sortable.png)


## Examples

You can find some examples under [samples-wrapper folder](https://github.com/miguelbcr/TableFixHeaders-Wrapper/tree/master/samples-wrapper)

## License

    Copyright 2016 miguelbcr

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.