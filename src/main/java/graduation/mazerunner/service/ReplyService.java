package graduation.mazerunner.service;

import graduation.mazerunner.domain.Reply;
import graduation.mazerunner.repository.PostRepository;
import graduation.mazerunner.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;

    public Reply findOne(Long id) {
        return replyRepository.findOne(id);
    }

    public Reply insertReply(Reply reply) {
        return replyRepository.save(reply);
    }

    public Reply updateReply(Reply reply) {
        Reply findReply = replyRepository.findOne(reply.getId());

        findReply.updateContent(reply.getContent());

        return findReply;
    }

    public List<Reply> findRecentReplies(String memberId) {
        return replyRepository.findRecentReplies(memberId);
    }

    public void deleteReply(Reply reply) {
        replyRepository.delete(reply.getId());
    }
}
