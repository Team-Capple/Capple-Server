package com.server.capple.domain.boardReport.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BoardReportType {

    BOARD_DISTRIBUTION_OF_ILLEGAL_PHOTOGRAPHS("불법촬영물 등의 유통"),
    BOARD_COMMERCIAL_ADVERTISING_AND_SALES("상업적 광고 및 판매"),
    BOARD_INADEQUATE_BOARD_CHARACTER("게시판 성격에 부적절함"),
    BOARD_ABUSIVE_LANGUAGE_AND_DISPARAGEMENT("욕설/비하"),
    BOARD_POLITICAL_PARTY_OR_POLITICIAN_DEMEANING_AND_CAMPAIGNING("정당/정치인 비하 및 선거운동"),
    BOARD_LEAK_IMPERSONATION_FRAUD("욕설/사칭/사기"),
    BOARD_TRICK_TEASING_PLASTERED("낚시, 놀림, 도배");

    private final String toKorean;
}
