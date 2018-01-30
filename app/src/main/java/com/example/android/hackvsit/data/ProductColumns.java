package com.example.android.hackvsit.data;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface ProductColumns {

    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType(INTEGER) @NotNull
    String ID = "id";

    @DataType(INTEGER) @NotNull
    String QUANTITY = "quantity";

    @DataType(INTEGER) @NotNull
    String MAX_QUANTITY = "max_quantity";

    @DataType(INTEGER) @NotNull
    String PRICE = "price";

    @DataType(TEXT) @NotNull
    String NAME = "name";

    @DataType(TEXT) @NotNull
    String IMAGE = "image";

}
