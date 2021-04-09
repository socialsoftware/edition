import React, { useState } from 'react';
/* import { setCorpusApi, getFragmentList } from '../../util/utilsAPI';
 */
const Admin = () => {

    const [corpus, setCorpus] = useState(null)
    const [corpus2, setCorpus2] = useState(null)
    
/*     const handleCorpusUpload = () => {
        var data = new FormData();
        data.append('file', corpus);
        //setCorpusApi(data)
        getFragmentList()
            .then(res => {
                console.log(res);
                setCorpus2(res.data)
            })
            .catch(err => {
                console.log("erro");
                console.log(err);
            })
    } */
    return (
        <div>
            <div>
					<input type="hidden" ></input>
					<div>
						<input type="file" name="file" onChange={(e) => {
                            console.log(e.target.files[0]);
                            setCorpus(e.target.files[0])}}></input>
					</div>
					<div>
	{/* 					<button onClick={() => handleCorpusUpload()}>
							oioi
						</button> */}
					</div>
                    
            </div>
        </div>
    )
}

export default Admin