import { Link } from "react-router-dom";

export default ({ taxonomies }) => {
  return (
    <div className="row" id="taxonomy">
      <table className="table table-hover">
        <thead>
          <tr>
            <th>
              <span className="glyphicon glyphicon-tag"></span>
            </th>
            <th>
              <span className="glyphicon glyphicon-user"></span>
            </th>
          </tr>
        </thead>
        <tbody>
          {taxonomies.map(({ user, category }, index) => (
            <tr key={index}>
              <td>
                {category && (
                  <Link
                    to={`/edition/acronym/${category.acronym}/category/${category.urlId}`}
                  >
                    {category.name}
                  </Link>
                )}
              </td>
              <td>
                {user && (
                  <Link to={`/edition/user/${user.userName}`}>
                    {`${user.firstName} ${user.lastName} (${user.userName})`}
                  </Link>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};
