package dk.sdu.se4.group1.CommonEcs;

import dk.sdu.se4.group1.CommonApi.SeedType;

public record SeedRequest(SeedType seedType, int x, int y) {
}
