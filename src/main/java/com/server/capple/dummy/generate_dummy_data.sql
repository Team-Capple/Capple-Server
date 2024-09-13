create procedure generate_dummy_data2(IN member_count integer, IN board_count integer)
    language plpgsql
as
$$
DECLARE
member_id INT;
    board_id INT;
    random_content TEXT;
    random_comment_count INT;
    random_board_type SMALLINT;
    random_value FLOAT;
BEGIN
    -- 1. 멤버 더미 데이터 생성
FOR member_id IN 1..member_count LOOP
            INSERT INTO member (nickname, email, sub, role, created_at, updated_at)
            VALUES (
                       'User' || member_id,
                       'user' || member_id || '@example.com',
                       'sub' || member_id,
                       'ROLE_ACADEMIER',
                       NOW(),
                       NULL
                   );
END LOOP;

    -- 2. 보드 더미 데이터 생성
FOR board_id IN 1..board_count LOOP
            random_content := 'This is a dummy content for board ' || board_id;
            random_comment_count := FLOOR(RANDOM() * 100);

            -- FREEBOARD와 HOTBOARD를 번갈아가며 설정 (0은 FREEBOARD, 1은 HOTBOARD)
            /*IF board_id % 2 = 0 THEN
                random_board_type := 0;
            ELSE
                random_board_type := 1;
            END IF;*/

            -- 보드 데이터 삽입
INSERT INTO board (member_id, board_type, content, comment_count, created_at, updated_at)
VALUES (
           FLOOR(RANDOM() * member_count) + 1,
           0,
           random_content,
           random_comment_count,
           NOW(),
           NULL
       );
END LOOP;

    -- 3. 보드하트 더미 데이터 생성
FOR board_id IN 1..board_count LOOP
            FOR member_id IN 1..member_count LOOP
                    random_value := RANDOM();
                    IF random_value < 0.5 THEN
                        INSERT INTO board_heart (board_id, member_id, is_liked, created_at, updated_at)
                        VALUES (
                                   board_id,
                                   member_id,
                                   TRUE,
                                   NOW(),
                                   NULL
                               );
END IF;
END LOOP;
END LOOP;

END;
$$;

alter procedure generate_dummy_data(integer, integer) owner to capple;

