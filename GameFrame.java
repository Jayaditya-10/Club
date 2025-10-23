// PlayerController.cs - basic player movement and shooting
using UnityEngine;

public class PlayerController : MonoBehaviour
{
    public float moveSpeed = 5f;
    public float lookSpeed = 3f;
    public Camera playerCamera;
    public GameObject bulletPrefab;
    public Transform bulletSpawn;
    public float bulletSpeed = 20f;

    void Update()
    {
        // Move the player
        float h = Input.GetAxis("Horizontal");
        float v = Input.GetAxis("Vertical");
        Vector3 move = transform.right * h + transform.forward * v;
        transform.position += move * moveSpeed * Time.deltaTime;

        // Rotate the player based on mouse movement
        float mouseX = Input.GetAxis("Mouse X") * lookSpeed;
        float mouseY = Input.GetAxis("Mouse Y") * lookSpeed;
        transform.Rotate(0, mouseX, 0);
        playerCamera.transform.Rotate(-mouseY, 0, 0);

        // Shoot bullet
        if (Input.GetButtonDown("Fire1"))
        {
            Shoot();
        }
    }

    void Shoot()
    {
        GameObject bullet = Instantiate(bulletPrefab, bulletSpawn.position, bulletSpawn.rotation);
        bullet.GetComponent<Rigidbody>().velocity = bulletSpawn.forward * bulletSpeed;
        Destroy(bullet, 2f);
    }
}

// BotAI.cs - simple bot that follows and shoots player
using UnityEngine;
using UnityEngine.AI;

public class BotAI : MonoBehaviour
{
    public Transform player;
    public float shootRange = 20f;
    public GameObject bulletPrefab;
    public Transform bulletSpawn;
    public float bulletSpeed = 20f;
    public float fireCooldown = 2f;
    private float nextFireTime;

    private NavMeshAgent agent;

    void Start()
    {
        agent = GetComponent<NavMeshAgent>();
    }

    void Update()
    {
        agent.SetDestination(player.position);
        float distance = Vector3.Distance(transform.position, player.position);
        if(distance <= shootRange && Time.time > nextFireTime)
        {
            Shoot();
            nextFireTime = Time.time + fireCooldown;
        }
    }

    void Shoot()
    {
        GameObject bullet = Instantiate(bulletPrefab, bulletSpawn.position, bulletSpawn.rotation);
        bullet.GetComponent<Rigidbody>().velocity = bulletSpawn.forward * bulletSpeed;
        Destroy(bullet, 2f);
    }
}

// GameManager.cs - manages game states like drops, spawns, and win condition
using UnityEngine;
using System.Collections.Generic;

public class GameManager : MonoBehaviour
{
    public GameObject playerPrefab;
    public GameObject botPrefab;
    public Transform[] spawnPoints;
    private List<GameObject> bots = new List<GameObject>();
    public int numBots = 10;

    void Start()
    {
        SpawnPlayer();
        SpawnBots();
    }

    void SpawnPlayer()
    {
        // Could be extended to drop the player from a plane with animation
        Instantiate(playerPrefab, spawnPoints[0].position, Quaternion.identity);
    }

    void SpawnBots()
    {
        for (int i = 1; i <= numBots; i++)
        {
            Vector3 spawnPos = spawnPoints[Random.Range(1, spawnPoints.Length)].position;
            GameObject bot = Instantiate(botPrefab, spawnPos, Quaternion.identity);
            bots.Add(bot);
        }
    }

    // Additional methods to track kills, deaths, and declare winner would go here
}
