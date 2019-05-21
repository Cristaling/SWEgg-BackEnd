package io.github.cristaling.swegg.backend.utils;

import com.github.javafaker.Faker;
import io.github.cristaling.swegg.backend.core.member.Member;
import io.github.cristaling.swegg.backend.core.member.MemberData;
import io.github.cristaling.swegg.backend.utils.enums.MemberRole;

public class TestingUtils {

	public static Member getFakeMember() {
		Member result = new Member();

		result.setEmail(Faker.instance().bothify("?????###@gmail.com"));
		result.setPassword(Faker.instance().leagueOfLegends().champion());
		result.setRole(MemberRole.CLIENT);
		result.setVerified(true);

		MemberData memberData = new MemberData();

		memberData.setBirthDate(Faker.instance().date().birthday());
		memberData.setTown(Faker.instance().address().cityName());
		memberData.setFirstName(Faker.instance().name().firstName());
		memberData.setLastName(Faker.instance().name().lastName());
		memberData.setMember(result);

		result.setMemberData(memberData);

		return result;
	}

}
