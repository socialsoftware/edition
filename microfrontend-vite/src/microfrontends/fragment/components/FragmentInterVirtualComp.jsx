import { Link } from 'react-router-dom';

export default ({ messages, fragmentInter }) => {
  return (
    <>
      <h4 className="text-center">
        {messages['virtual_edition_comparison']}
      </h4>
      <br />
      {fragmentInter.taxonomiesComp?.map((tax, ind) => (
        <div key={ind} className="row col-md-12">
          <strong>{messages.edition}: </strong>
          {fragmentInter.inters[ind]?.reference}
          <table className="table table-bordered table-striped table-condensed">
            <thead>
              <tr>
                <th>{messages.citation}</th>
                <th>{messages.comment}</th>
                <th>{messages.user}</th>
                <th>{messages.tags}</th>
              </tr>
            </thead>
            <tbody>
              {tax?.map(({ citation, comment, user, category }, ind) => (
                <tr key={ind}>
                  <td>{citation ?? '---'}</td>
                  <td>{comment ?? '---'}</td>
                  <td>
                    <span className="glyphicon glyphicon-user"></span>
                    {user &&
                    <Link to={`/edition/acronym/user/${user.userName}`}>
                      {`  ${user.userName}`}
                    </Link>}
                  </td>
                  <td>
                    <span className="glyphicon glyphicon-tag"></span>
                    {category && <Link
                      to={`/edition/acronym/${category.acronym}/category${category.urlId}`}
                    >
                      {`  ${category.name}`}
                    </Link>}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      ))}
    </>
  );
};
