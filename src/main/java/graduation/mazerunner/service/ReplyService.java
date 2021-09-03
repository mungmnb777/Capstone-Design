package graduation.mazerunner.service;

import graduation.mazerunner.domain.Reply;
import graduation.mazerunner.repository.PostRepository;
import graduation.mazerunner.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Reply findOne(Reply reply) {
        return replyRepository.findOne(reply.getId());
    }

    public Reply insertReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public Reply updateReply(Reply reply) {
        Reply findReply = replyRepository.findOne(reply.getId());

        findReply.updateContent(reply.getContent());

        return findReply;
    }

    public void deleteReply(Reply reply) {
        replyRepository.delete(reply.getId());
    }
}
