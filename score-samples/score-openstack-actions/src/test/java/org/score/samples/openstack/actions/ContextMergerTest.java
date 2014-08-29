package org.score.samples.openstack.actions;


import com.hp.score.lang.ExecutionRuntimeServices;
import org.junit.Test;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


/**
 * Date: 8/11/2014
 *
 * @author lesant
 */

public class ContextMergerTest {

	private static final long DEFAULT_TIMEOUT = 5000;
	private static final String CREATE_SERVER_RESPONSE_MOCK = "{\"access\": {\"token\": {\"issued_at\": \"2014-08-12T11:22:22.033360\", \"expires\": \"2014-08-12T12:22:22Z\", \"id\": \"PKIZ_eJy1WFt3oswSfe9fcd6_NSuA6IRHpBFh7DYocuk3AQM0oE68IPz6U6BJTCaZXM75ksVyCdJU7aq9exc_fsDfUDdM-h-NzNsvPxAxTduuAuvXhpnpMaKqHWkbOEexpulY1VRbP2mOOh4miwecEM3gmoa3uaHNfxtzM-whbOtjdQknNxs9SQbGqrbSQKJF1CPZNBv-NNez41JyDybfJHBtG_WoyLzZfeDbcF3NEOHqiWpCNdWEEynsE3E2NeFwZHJFGtJQrmYTTc2Yn1ZL3yqi2hyYmlkTrAsTR5XRxNFrF-v1lJvd4WXmztSsLdPaH1pVaIw489JjaMyK6HxNYF4_Ddd2FyEyS7gg0XrpD4Wlp3Shhmt3F2pVYpbuIfCsHfO6HwvR2i3aaJbeOXziuSWaevqJ4lwMGsjOWPQpL1KGWc6cRGIOKwk2RYajLo2wHO3ZvIssZ554NNeigtoLkeTWcVlwtqBpbIx2oRS1TwwBtCIsZ1UoFYd4TB7PpcwQt2Hhzh1IE8Fqm3g8q6bZ7ZE4wYE6ORzJAcCtpjzp0cY-xtw8tuG-FS16L9yodNePmJiFVTCpOIa5coAUOpDDNWBX9lMUjs6hfDcS9BFwT4g7w4yUgchK-HeSmhqzlPGUQwqJEEhBQ_iwIBLlhCfnFMZuFhoFd12LdGmUqRCP1cGkvj3RTBanczg0uUYEq4MpBlA0RSLZ7Yl5QT9wdGHquJw2kcCcXJp6o5xCv0wxK2gZVIS7Oa0txZ9XCWqbhY2Hx6XXFyJR2cHnQ9euo33cRhKPi-pSex5KYhX7s8LM2iaDbvQWGWrzC0tFCuaWMtGAPNcLnheCc6M89IrDW-mg63ye0sk-nw4659PVtmZevA0luUN9cekDR-pfQoZuNNwa2nl3HQl6C9mvRIJeIntNZbMiTtQnTsFJaZ-CJocFbCEo9ZoAxakTVVSyBUQaXWJnnlcxUHfpUXeRV10ay_FMiPDmOIHfTPiiD4c84bAw3sgQiTTpBTWaNKOCQScSx-5Tj0DLAkAOhOzZEjMWMi0XEuNqTT27B_TtSguCkkeG0vKhQb502oblvmlB8UShU5yVPyw6kNbsGI7dPfOD-rr2bXQBUJwBqAgIkt1DR32G-28RDv3JOPJv0pm8YChtAhEBWDJ1RmngsYxityBNWtAG5A3bDXHymkh6n0rkXYb-vZFahl5Y9x7p0N9Y9xjylRofwh5L2_OPwKPvIP-MDWmgE7-G-mvQ0XsrPy78hDgm8hTrInXUUyBZBTWCHsGJDBGMQCRNaF0rDxobyKN-XRO7532A9htgwz3mrt2ZYAMVL71PGnMNfBjvXhCm40lvlwXebA8LvmAreoeufcLzM4HWF0Cz7qnzs2r178Jy0aWK2lCZ30Zx-owOPC9cFnkbGTK5Dk7EAiDdlDoWD5xZSj0rA23oB3zICYYWN2YlKd2zNq6HYlCetoH4qd05r6cO0N3tdOKK7vQeKnRYgo9Cbfd16K9n_cg4i8XSE1MmLV638RrsziGA89f6gb7fxglgoh5BkfR_o5XPqz8u_rzFkxNzdJnxRKCODR7BlMFkmT2GF_1pi3yr0CAwjNufbmf0LCBqRVoBmX-tpVEnIIYiMoMdo1IEl9Z13e5Fi5dp0Vrbt9QbfQN9ENigxef4ZaP5VqXQZ0r1t0jQYyhfieRJpXgCDgWnJeEwITh2AwvUzLA43JATbnGK4Slc7RMj-Oa-0BqM-ZcMxpdlTQIyncR2y37FPA435cz_eOdGn689OT6NBT452p0-yhn6X5jYPgx96mnS6Rh79ktm4uhEHdIDEGc5w2CzMYwxYLxbN07AmRI-ylufBOX9KzPReyUktdKAWkvg2mDUU3BHOO3PKqEPylQEjfm6Qt25_9P2noBXxvbxO3x4rBz6TumuI0HPIvq9SNBzXVVQ3ItH8FpmmjLcmAdOIJCWWNjKgtLNGH_p2NBHBuJRcDvH9gYz0UdD1UfMRFc1B1WmG-ZTISpH50n-lcFeGm4aay9nK_TZ4ep1WqRZNDB0tJpoHoj2L8xMYOfOO1UmV9eOxORmL2gW_aDRYeLVYXbmMxgezD5INJxYiKRJ-sxzs6AJqrccyeuGQi97e3HZldu0vl6FLYwwkGchrOav98Z9sYLqhC0el7kp9mn7UgY2V_CE3Zfreeiid2fSKDso7ZMnerJEzy4tksEfyBRaOXDsmnAYNDxwajyRwaEVAddrBpsMACddfOMxNNwn84muhGLDvJHg96gQePTBl5ReWLr1o3O9-t1ZL7Kn8f_P0O-7EMUiNkAX_Vn69BpMVC43w0JY7Xqn3VjOr7dGu4xKwAGcQEmjmjQ5pEXqoDRFatB8akDnYV0iPIa99EzlgOcVkAnybdQeaT8xzZih9wJH7SbXoElgIb0HLZv5vqjcO4amBnqVJCNFTfQhGVUVcht9QtTcUMWFPkyJ5rrkhLlKhgl1h2pChq57gD1CILZcYTXArm2Pseq64ZoWMdaniKhVd7OWVCPX64OU2ycDq955AUJwL-7Fdb9YGaN9ZJyKSUmPoW0O1cs7RnR-yajSYZL_TvPMUCphqNr6SFXnmmoPjz8fmOIou3wuLGDgeRDWB8G34pXH01NR3p1ClK_2yr27264e7pgsZqMko-yhwGN3vLplHh33b3fbjRo9PNwkkwHI5IDOzA1zSPIrT0U8H6D9-k4VVgujLsfR4R5rPWs8k29_DvjNSCPHoriXUz7v74-_ppGuGofQq25iHC7x1JnV2sAqYfTlai3unOFoFk43owXb3cYPYUXLdTiQJmG-GS4tc1d4yWo6ML01H9_cGLfD7ex2FZf-NO-hm2mVRDeaJEtlsV95B1XTFneHf9Kfv8tfy_WNtyGOFw0U_y5aqRPlfr5aruKZOPF_C-u4csQlcqzboWv8nhTyXRL51exepbhRH4SVYyY_lcX-H8_F_jqnxN1ad319ZwnSBHUvhHWKn18O_xfNtmcu\", \"tenant\": {\"description\": null, \"enabled\": true, \"id\": \"1ef9a1495c774e969ad6de86e6f025d7\", \"name\": \"demo\"}}, \"serviceCatalog\": [{\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7\", \"id\": \"10b2f5fffe824daf8cd84cf320e3cc28\", \"publicURL\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7\"}], \"endpoints_links\": [], \"type\": \"compute\", \"name\": \"nova\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8776/v2/1ef9a1495c774e969ad6de86e6f025d7\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8776/v2/1ef9a1495c774e969ad6de86e6f025d7\", \"id\": \"017919c2d1c94944ba20ac5707d4316f\", \"publicURL\": \"http://16.59.58.200:8776/v2/1ef9a1495c774e969ad6de86e6f025d7\"}], \"endpoints_links\": [], \"type\": \"volumev2\", \"name\": \"cinderv2\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8774/v3\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8774/v3\", \"id\": \"765ad851aafb45e38e79cd431923a97c\", \"publicURL\": \"http://16.59.58.200:8774/v3\"}], \"endpoints_links\": [], \"type\": \"computev3\", \"name\": \"novav3\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:3333\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:3333\", \"id\": \"438815501cbe4f708831fe29abdc4910\", \"publicURL\": \"http://16.59.58.200:3333\"}], \"endpoints_links\": [], \"type\": \"s3\", \"name\": \"s3\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:9292\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:9292\", \"id\": \"1032fe5a52ca4a5bb5c9b0c06b4df2ee\", \"publicURL\": \"http://16.59.58.200:9292\"}], \"endpoints_links\": [], \"type\": \"image\", \"name\": \"glance\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8000/v1\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8000/v1\", \"id\": \"131e18f845414289b7d599bb71eed8f4\", \"publicURL\": \"http://16.59.58.200:8000/v1\"}], \"endpoints_links\": [], \"type\": \"cloudformation\", \"name\": \"heat\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8776/v1/1ef9a1495c774e969ad6de86e6f025d7\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8776/v1/1ef9a1495c774e969ad6de86e6f025d7\", \"id\": \"68148f266543492dbce6d22c4876090f\", \"publicURL\": \"http://16.59.58.200:8776/v1/1ef9a1495c774e969ad6de86e6f025d7\"}], \"endpoints_links\": [], \"type\": \"volume\", \"name\": \"cinder\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8773/services/Admin\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8773/services/Cloud\", \"id\": \"07153784dd3642dd84fff2ad21dad654\", \"publicURL\": \"http://16.59.58.200:8773/services/Cloud\"}], \"endpoints_links\": [], \"type\": \"ec2\", \"name\": \"ec2\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:8004/v1/1ef9a1495c774e969ad6de86e6f025d7\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:8004/v1/1ef9a1495c774e969ad6de86e6f025d7\", \"id\": \"001e2dc491d342869da6407742bbebf3\", \"publicURL\": \"http://16.59.58.200:8004/v1/1ef9a1495c774e969ad6de86e6f025d7\"}], \"endpoints_links\": [], \"type\": \"orchestration\", \"name\": \"heat\"}, {\"endpoints\": [{\"adminURL\": \"http://16.59.58.200:35357/v2.0\", \"region\": \"RegionOne\", \"internalURL\": \"http://16.59.58.200:5000/v2.0\", \"id\": \"27c59c14ca764bcb9764ce5389eebc60\", \"publicURL\": \"http://16.59.58.200:5000/v2.0\"}], \"endpoints_links\": [], \"type\": \"identity\", \"name\": \"keystone\"}], \"user\": {\"username\": \"admin\", \"roles_links\": [], \"id\": \"78d58401a422431da68806eb12f2cc56\", \"roles\": [{\"name\": \"heat_stack_owner\"}, {\"name\": \"admin\"}], \"name\": \"admin\"}, \"metadata\": {\"is_admin\": 0, \"roles\": [\"7ed489772398432bb54cd8e341627f27\", \"b90e4a70714a43bda7a09ad6c8231757\"]}}}";
	private static final String GET_SERVERS_RESPONSE_MOCK = "{\"servers\": [{\"id\": \"89f60390-0a0f-4e8d-9e0c-cba4d40bae6d\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/89f60390-0a0f-4e8d-9e0c-cba4d40bae6d\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/89f60390-0a0f-4e8d-9e0c-cba4d40bae6d\", \"rel\": \"bookmark\"}], \"name\": \"testserver2\"}, {\"id\": \"713f2cd3-b5a1-4b31-aa68-e058b6fa8359\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/713f2cd3-b5a1-4b31-aa68-e058b6fa8359\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/713f2cd3-b5a1-4b31-aa68-e058b6fa8359\", \"rel\": \"bookmark\"}], \"name\": \"testserver\"}, {\"id\": \"598db5ed-eb03-4fd8-b9c8-f3848a13fb52\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/598db5ed-eb03-4fd8-b9c8-f3848a13fb52\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/598db5ed-eb03-4fd8-b9c8-f3848a13fb52\", \"rel\": \"bookmark\"}], \"name\": \"myDemoServer\"}, {\"id\": \"edc51a61-8ca2-4a63-ad74-02980ca15c4f\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/edc51a61-8ca2-4a63-ad74-02980ca15c4f\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/edc51a61-8ca2-4a63-ad74-02980ca15c4f\", \"rel\": \"bookmark\"}], \"name\": \"myServer2\"}, {\"id\": \"ef3c501d-f566-471d-b73d-893a46b4516b\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/ef3c501d-f566-471d-b73d-893a46b4516b\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/ef3c501d-f566-471d-b73d-893a46b4516b\", \"rel\": \"bookmark\"}], \"name\": \"myServer1\"}, {\"id\": \"d0b7442d-73fc-4578-b595-04c4c93f9528\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/d0b7442d-73fc-4578-b595-04c4c93f9528\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/d0b7442d-73fc-4578-b595-04c4c93f9528\", \"rel\": \"bookmark\"}], \"name\": \"meshi3\"}, {\"id\": \"8bfc42c7-2d2b-4053-98f9-873561169c74\", \"links\": [{\"href\": \"http://16.59.58.200:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers/8bfc42c7-2d2b-4053-98f9-873561169c74\", \"rel\": \"self\"}, {\"href\": \"http://16.59.58.200:8774/1ef9a1495c774e969ad6de86e6f025d7/servers/8bfc42c7-2d2b-4053-98f9-873561169c74\", \"rel\": \"bookmark\"}], \"name\": \"to-make-meshi-happy\"}]}";
	private static final String EXPECTED_TENANT = "1ef9a1495c774e969ad6de86e6f025d7";
	private static final int EXPECTED_SERVER_SIZE = 7;
	private static final String EXPECTED_FIRST_SERVER = "testserver2";
	private static final String EXPECTED_TOKEN = "PKIZ_eJy1WFt3oswSfe9fcd6_NSuA6IRHpBFh7DYocuk3AQM0oE68IPz6U6BJTCaZXM75ksVyCdJU7aq9exc_fsDfUDdM-h-NzNsvPxAxTduuAuvXhpnpMaKqHWkbOEexpulY1VRbP2mOOh4miwecEM3gmoa3uaHNfxtzM-whbOtjdQknNxs9SQbGqrbSQKJF1CPZNBv-NNez41JyDybfJHBtG_WoyLzZfeDbcF3NEOHqiWpCNdWEEynsE3E2NeFwZHJFGtJQrmYTTc2Yn1ZL3yqi2hyYmlkTrAsTR5XRxNFrF-v1lJvd4WXmztSsLdPaH1pVaIw489JjaMyK6HxNYF4_Ddd2FyEyS7gg0XrpD4Wlp3Shhmt3F2pVYpbuIfCsHfO6HwvR2i3aaJbeOXziuSWaevqJ4lwMGsjOWPQpL1KGWc6cRGIOKwk2RYajLo2wHO3ZvIssZ554NNeigtoLkeTWcVlwtqBpbIx2oRS1TwwBtCIsZ1UoFYd4TB7PpcwQt2Hhzh1IE8Fqm3g8q6bZ7ZE4wYE6ORzJAcCtpjzp0cY-xtw8tuG-FS16L9yodNePmJiFVTCpOIa5coAUOpDDNWBX9lMUjs6hfDcS9BFwT4g7w4yUgchK-HeSmhqzlPGUQwqJEEhBQ_iwIBLlhCfnFMZuFhoFd12LdGmUqRCP1cGkvj3RTBanczg0uUYEq4MpBlA0RSLZ7Yl5QT9wdGHquJw2kcCcXJp6o5xCv0wxK2gZVIS7Oa0txZ9XCWqbhY2Hx6XXFyJR2cHnQ9euo33cRhKPi-pSex5KYhX7s8LM2iaDbvQWGWrzC0tFCuaWMtGAPNcLnheCc6M89IrDW-mg63ye0sk-nw4659PVtmZevA0luUN9cekDR-pfQoZuNNwa2nl3HQl6C9mvRIJeIntNZbMiTtQnTsFJaZ-CJocFbCEo9ZoAxakTVVSyBUQaXWJnnlcxUHfpUXeRV10ay_FMiPDmOIHfTPiiD4c84bAw3sgQiTTpBTWaNKOCQScSx-5Tj0DLAkAOhOzZEjMWMi0XEuNqTT27B_TtSguCkkeG0vKhQb502oblvmlB8UShU5yVPyw6kNbsGI7dPfOD-rr2bXQBUJwBqAgIkt1DR32G-28RDv3JOPJv0pm8YChtAhEBWDJ1RmngsYxityBNWtAG5A3bDXHymkh6n0rkXYb-vZFahl5Y9x7p0N9Y9xjylRofwh5L2_OPwKPvIP-MDWmgE7-G-mvQ0XsrPy78hDgm8hTrInXUUyBZBTWCHsGJDBGMQCRNaF0rDxobyKN-XRO7532A9htgwz3mrt2ZYAMVL71PGnMNfBjvXhCm40lvlwXebA8LvmAreoeufcLzM4HWF0Cz7qnzs2r178Jy0aWK2lCZ30Zx-owOPC9cFnkbGTK5Dk7EAiDdlDoWD5xZSj0rA23oB3zICYYWN2YlKd2zNq6HYlCetoH4qd05r6cO0N3tdOKK7vQeKnRYgo9Cbfd16K9n_cg4i8XSE1MmLV638RrsziGA89f6gb7fxglgoh5BkfR_o5XPqz8u_rzFkxNzdJnxRKCODR7BlMFkmT2GF_1pi3yr0CAwjNufbmf0LCBqRVoBmX-tpVEnIIYiMoMdo1IEl9Z13e5Fi5dp0Vrbt9QbfQN9ENigxef4ZaP5VqXQZ0r1t0jQYyhfieRJpXgCDgWnJeEwITh2AwvUzLA43JATbnGK4Slc7RMj-Oa-0BqM-ZcMxpdlTQIyncR2y37FPA435cz_eOdGn689OT6NBT452p0-yhn6X5jYPgx96mnS6Rh79ktm4uhEHdIDEGc5w2CzMYwxYLxbN07AmRI-ylufBOX9KzPReyUktdKAWkvg2mDUU3BHOO3PKqEPylQEjfm6Qt25_9P2noBXxvbxO3x4rBz6TumuI0HPIvq9SNBzXVVQ3ItH8FpmmjLcmAdOIJCWWNjKgtLNGH_p2NBHBuJRcDvH9gYz0UdD1UfMRFc1B1WmG-ZTISpH50n-lcFeGm4aay9nK_TZ4ep1WqRZNDB0tJpoHoj2L8xMYOfOO1UmV9eOxORmL2gW_aDRYeLVYXbmMxgezD5INJxYiKRJ-sxzs6AJqrccyeuGQi97e3HZldu0vl6FLYwwkGchrOav98Z9sYLqhC0el7kp9mn7UgY2V_CE3Zfreeiid2fSKDso7ZMnerJEzy4tksEfyBRaOXDsmnAYNDxwajyRwaEVAddrBpsMACddfOMxNNwn84muhGLDvJHg96gQePTBl5ReWLr1o3O9-t1ZL7Kn8f_P0O-7EMUiNkAX_Vn69BpMVC43w0JY7Xqn3VjOr7dGu4xKwAGcQEmjmjQ5pEXqoDRFatB8akDnYV0iPIa99EzlgOcVkAnybdQeaT8xzZih9wJH7SbXoElgIb0HLZv5vqjcO4amBnqVJCNFTfQhGVUVcht9QtTcUMWFPkyJ5rrkhLlKhgl1h2pChq57gD1CILZcYTXArm2Pseq64ZoWMdaniKhVd7OWVCPX64OU2ycDq955AUJwL-7Fdb9YGaN9ZJyKSUmPoW0O1cs7RnR-yajSYZL_TvPMUCphqNr6SFXnmmoPjz8fmOIou3wuLGDgeRDWB8G34pXH01NR3p1ClK_2yr27264e7pgsZqMko-yhwGN3vLplHh33b3fbjRo9PNwkkwHI5IDOzA1zSPIrT0U8H6D9-k4VVgujLsfR4R5rPWs8k29_DvjNSCPHoriXUz7v74-_ppGuGofQq25iHC7x1JnV2sAqYfTlai3unOFoFk43owXb3cYPYUXLdTiQJmG-GS4tc1d4yWo6ML01H9_cGLfD7ex2FZf-NO-hm2mVRDeaJEtlsV95B1XTFneHf9Kfv8tfy_WNtyGOFw0U_y5aqRPlfr5aruKZOPF_C-u4csQlcqzboWv8nhTyXRL51exepbhRH4SVYyY_lcX-H8_F_jqnxN1ad319ZwnSBHUvhHWKn18O_xfNtmcu";

	@Test(timeout = DEFAULT_TIMEOUT)
	public void testGetToken(){

		ContextMerger merger = new ContextMerger();
		String actualToken = merger.getToken(CREATE_SERVER_RESPONSE_MOCK);
		assertEquals("Token not computed as expected.", EXPECTED_TOKEN, actualToken);
	}
	@Test(timeout = DEFAULT_TIMEOUT)
	public void testGetTenant(){

		ContextMerger merger = new ContextMerger();
		String actualTenant = merger.getTenant(CREATE_SERVER_RESPONSE_MOCK);
		assertEquals("Tenant not computed as expected.", EXPECTED_TENANT, actualTenant);

	}
	@Test(timeout = DEFAULT_TIMEOUT)
	public void testGetServerList(){

		ContextMerger merger = new ContextMerger();
		List<String> serverList = merger.getServerList(GET_SERVERS_RESPONSE_MOCK);
		assertEquals("Number of servers is incorrect.",  EXPECTED_SERVER_SIZE,  serverList.size());
		assertEquals("First server not as expected.",  EXPECTED_FIRST_SERVER,  serverList.get(0));


	}
	@Test(timeout = DEFAULT_TIMEOUT)
	public void testPrepareCreateServer(){
		ContextMerger merger = new ContextMerger();

		Map<String, Serializable> executionContext = new HashMap<>();
		String returnResult = CREATE_SERVER_RESPONSE_MOCK;
		String serverName = "serverName";
		executionContext.put("returnResult", returnResult);
		executionContext.put("serverName", serverName);
		executionContext.put("host", "url");
		executionContext.put("computePort", "");
		executionContext.put("identityPort", "");
		executionContext.put("imgRef", "");

		String expectedBody = "{\"server\": {\"name\": \""+ serverName+"\",\"imageRef\": \"56ff0279-f1fb-46e5-93dc-fe7093af0b1a\",\"flavorRef\": \"2\",\"max_count\": 1,\"min_count\": 1,\"security_groups\": [{\"name\": \"default\"}]}}";
		String expectedUrl = "http://url:8774/v2/1ef9a1495c774e969ad6de86e6f025d7/servers";
		String expectedHeaders = "X-AUTH-TOKEN: " + merger.getToken(CREATE_SERVER_RESPONSE_MOCK);
		ExecutionRuntimeServices executionRuntimeServices = new ExecutionRuntimeServices();
		Map<String, String> returnMap = merger.prepareCreateServer(executionContext, executionRuntimeServices, "");


		assertEquals("URL not as expected.", expectedUrl, returnMap.get("url"));
		assertEquals("Headers not as expected.", expectedHeaders, returnMap.get("headers"));
		assertEquals("Body not as expected.", expectedBody, returnMap.get("body"));
	}

}
