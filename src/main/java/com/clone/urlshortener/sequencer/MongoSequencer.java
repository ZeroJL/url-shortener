package com.clone.urlshortener.sequencer;

import com.clone.urlshortener.sequencer.model.Sequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RequiredArgsConstructor
@Service
public class MongoSequencer implements Sequencer{
    private final MongoOperations mongoOperations;

    @Override
    public Long getSequence(String sequenceName) {
        Sequence sequence = mongoOperations.findAndModify(
                Query.query(where("_id").is(sequenceName)),
                new Update().inc("sequence", 1),
                options().returnNew(true).upsert(true),
                Sequence.class
        );

        return sequence.getSequence();
    }
}
