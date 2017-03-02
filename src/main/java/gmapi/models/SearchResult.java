package gmapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {

    private GMObject gmObject;

    private boolean bestResult;
    private boolean navigationalResult;

}
