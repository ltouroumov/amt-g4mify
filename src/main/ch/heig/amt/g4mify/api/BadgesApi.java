package ch.heig.amt.g4mify.api;

import ch.heig.amt.g4mify.model.Badge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ldavid
 * @created 12/5/16
 */
@RestController
@RequestMapping("/api/users/{userId}/badges")
public class BadgesApi extends AbstractDomainApi {

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Badge>> index(@PathVariable long userId,
                                             @RequestParam(required = false, defaultValue = "0") long page,
                                             @RequestParam(required = false, defaultValue = "50") long pageSize) {
        return ResponseEntity.ok(new ArrayList<>());
    }

}
