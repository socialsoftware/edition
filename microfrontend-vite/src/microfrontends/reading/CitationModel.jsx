import { Link } from "react-router-dom";

export function TwiterCitation({
  formatedDate,
  sourceLink,
  username,
  title,
  xmlId,
  tweetText,
  location,
  country
}) {
  const result =  {
    date: `${formatedDate[2]}-${formatedDate[1]}-${formatedDate[0]} ${formatedDate[3]}:${formatedDate[4]}`,
    title: <Link to={`/fragments/fragment/${xmlId}`}>{title}</Link>,
    sourceLink: <a href={sourceLink} target="_blank">Tweet</a>,
    tweetText,
    location,
    country,
    username: <a href={`https://twitter.com/${username}`} target="_blank">{username}</a>,
  };
  return {...result, searchData: JSON.stringify(result)}
}

