<h2>Decline reservation #${reservation.id}</h2>

<form method="post" action="${pageContext.request.contextPath}/reservations">

    <input type="hidden" name="action" value="decline"/>
    <input type="hidden" name="id" value="${reservation.id}"/>

    <label>Reason of refusal :</label><br/>
    <textarea name="reason" required></textarea>

    <br/><br/>

    <button type="submit">Confirm decline</button>
</form>
