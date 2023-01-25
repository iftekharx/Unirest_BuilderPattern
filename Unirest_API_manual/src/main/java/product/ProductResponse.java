package product;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private final String title;
    private final int price;
    private final String description;
    private int categoryId;
    private final String[] images;
}
