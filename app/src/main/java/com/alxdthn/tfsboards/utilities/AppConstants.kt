package com.alxdthn.tfsboards.utilities

object AppConstants {

	const val	UNAUTHORIZED			= 401
	const val	UNKNOWN_ERROR			= 0

	const val	DELETE_CARD_REQUEST		= 1
	const val	DELETE_COLUMN_REQUEST	= 2

	const val	APP_HOST				= "tfsboards"

	const val	TEAMS_FRAGMENT			= "boards_list"
	const val	BOARD_FRAGMENT			= "board"
	const val	AUTH_FRAGMENT			= "not_auth"
	const val	CARD_FRAGMENT			= "card"
	const val	BOARD_MEMBERS_FRAGMENT	= "board_members"
	const val	NO_CONNECTION_FRAGMENT	= "no_internet"
	const val	FILTER_FRAGMENT			= "filter"

	const val	TEAMS_KEY				= "teams_key"
	const val	COLORS_KEY				= "board_color_key"

	const val	CARD_MEMBERS_KEY		= "card_members_key"

	const val	BOARD_KEY				= "board_key"
	const val	BOARD_ID_KEY			= "board_id_key"
	const val	BOARD_NAME_KEY			= "board_name_key"
	const val	BOARD_BACKGROUND_KEY	= "board_background_key"
	const val	BOARD_MEMBERS_KEY		= "board_members_key"

	const val	COLUMN_NAME_KEY			= "column_name_key"
	const val	CARD_KEY				= "card_key"

	const val	COLOR_KEY				= "header_color_key"
	const val	DIALOG_MESSAGE_KEY		= "dialog_message_key"
	const val	DIALOG_ITEM_ID_KEY		= "dialog_item_id_key"
	const val	REQUEST_CODE_KEY		= "request_code_key"
	const val	FRAGMENT_TAG_KEY		= "fragment_tag_key"

	const val	INTERNAL_PREFS			= "com.alxdthn.$APP_HOST.prefs"
	const val	TOKEN_PREF_KEY			= "token_pref_key"

	const val	BASE_URL 				= "https://api.trello.com/1/"
	const val	USER_REQUEST_FIELDS 	= "username,fullName,avatarHash"
	const val	TEAM_REQUEST_FIELDS 	= "displayName"
	const val	BOARD_REQUEST_FIELDS	= "name,idOrganization,prefs"
	const val	LIST_REQUEST_FIELDS 	= "name,idBoard,pos,closed"
	const val	CARD_REQUEST_FIELDS 	= "name,idList,pos,closed,desc,idMembers"

	const val	AVATAR_SIZE				= "170"

	const val	APP_KEY 				= "ae903b942632ba9d72b9a092e2e338ab"
	const val	AVATAR_URL				= "https://trello-avatars.s3.amazonaws.com"
	const val	AUTH_URL				= "https://trello.com/1/authorize"
	const val	EXPIRATION				= "never"
	const val	SCOPE					= "read,write,account"
	const val	RESPONSE_TYPE			= "token"
	const val	NAME					= "TFS Boards"
	const val	CALLBACK_METHOD			= "fragment"
	const val	RETURN_URL				= "https://$APP_HOST"
	const val 	PREPARED_AUTH_URL		= "$AUTH_URL?key=$APP_KEY&expiration=$EXPIRATION&scope=$SCOPE&response_type=$RESPONSE_TYPE&name=$NAME&callback_method=$CALLBACK_METHOD&return_url=$RETURN_URL"
}